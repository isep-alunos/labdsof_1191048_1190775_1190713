import Cookies from "universal-cookie";

const cookies = new Cookies();

const apiUrl = process.env.REACT_APP_API_URL;
export default class HttpService {
    async get<T>(path: string): Promise<httpResponse<T>> {
        const response = await fetch(apiUrl + path);

        return responseObject(response);
    }

    async getPrivate<T>(path: string): Promise<httpResponse<T>> {
        await this.checkTokenExpiration();
        const response = await fetch(apiUrl + path, {
            headers: {Authorization: "Bearer " + cookies.get("token")},
        });

        return responseObject(response);
    }

    async postPrivate<T>(path: string, body = {}): Promise<httpResponse<T>> {
        await this.checkTokenExpiration();
        const response = await fetch(apiUrl + path, {
            method: "POST",
            headers: {
                Authorization: "Bearer " + cookies.get("token"),
                "Content-Type": "application/json",
            },
            body: JSON.stringify(body),
        });

        return responseObject(response);
    }

    async putPrivate<T>(path: string, body = {}): Promise<httpResponse<T>> {
        await this.checkTokenExpiration();
        const response = await fetch(apiUrl + path, {
            method: "PUT",
            headers: {
                Authorization: "Bearer " + cookies.get("token"),
                "Content-Type": "application/json",
            },
            body: JSON.stringify(body),
        });

        return responseObject(response);
    }

    async deletePrivate<T>(path: string, body = {}): Promise<httpResponse<T>> {
        await this.checkTokenExpiration();
        const response = await fetch(apiUrl + path, {
            method: "DELETE",
            headers: {
                Authorization: "Bearer " + cookies.get("token"),
                "Content-Type": "application/json",
            },
            body: JSON.stringify(body),
        });

        return responseObject(response);
    }

    async getToken(code: string): Promise<tokenResponse> {
        try {
            const response = await fetch(
                apiUrl + "/auth/callback?code=" + code
            );
            if (response.ok) {
                const data = await response.json();
                cookies.set("token", data.token, {path: "/"});
                cookies.set("refreshToken", data.refreshToken, {path: "/"});
                cookies.set("name", data.name, {path: "/"});
                cookies.set("picture", data.picture, {path: "/"});
                cookies.set("isAdmin", data.admin, {path: "/"});
                cookies.set("expireDate", data.expireDate, {path: "/"});
                return {
                    success: true,
                    token: data.token,
                    refreshToken: data.refreshToken,
                    name: data.name,
                    picture: data.picture,
                    isAdmin: data.admin,
                    expireDate: data.expireDate,
                };
            } else {
                console.error("Failed to fetch token");
                console.log(response);
                return {success: false};
            }
        } catch (error) {
            console.error("Error fetching token:", error);
            return {success: false};
        }
    }

    async checkTokenExpiration() {
        const expireDate = cookies.get("expireDate");
        if (expireDate) {
            const expireDateObj = new Date(expireDate);
            if (expireDateObj < new Date()) {
                console.log("Token expired, fetching new token using refresh token");
                try {
                    const response = await fetch(
                        apiUrl + "/auth/refresh?refreshToken=" +
                        cookies.get("refreshToken")
                    );
                    if (response.ok) {
                        const data = await response.json();
                        cookies.set("token", data.token, {path: "/"});
                        cookies.set("expireDate", data.expireDate, {path: "/"});
                    } else {
                        console.error("Failed to fetch token from refresh token");
                        console.log(response);
                    }
                } catch (error) {
                    console.error("Error fetching token from refresh token:", error);
                }
            } else {
                console.log("Token is still valid");
            }
        }
    }

    logout() {
    }
}

async function responseObject<T>(response: Response): Promise<httpResponse<T>> {
    if (response.status === 204) {
        return {code: response.status};
    }

    return {code: response.status, body: await response.json()};
}
