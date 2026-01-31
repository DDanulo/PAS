import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080/PAS_SPA/api/v1',
    headers: {'Content-Type': 'application/json'},
});

axiosInstance.interceptors.request.use((config) => {
    const token = sessionStorage.getItem('access_token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});
// const axiosSetup = axios.create({
//     baseURL: 'http://localhost:8080/PAS_PD-1/api/v1',
//     headers: {
//         'Content-Type': 'application/json',
//     },
// });

axiosInstance.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        if ((error.response?.status === 401 || error.response?.status === 403) && !originalRequest._retry) {
            originalRequest._retry = true;
            const refToken = sessionStorage.getItem('refresh_token');

            if (refToken) {
                try {
                    const res = await axios.post('http://localhost:8080/PAS_SPA/api/v1/auth/refresh', {
                        refreshToken: refToken
                    });

                    if (res.status === 200) {
                        const {accessToken, refreshToken: newRefreshToken} = res.data;

                        sessionStorage.setItem('access_token', accessToken);
                        sessionStorage.setItem('refresh_token', newRefreshToken);

                        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
                        return axiosInstance(originalRequest);
                    }
                } catch (refreshError) {
                    sessionStorage.clear();
                    window.location.href = '/login';
                    return Promise.reject(refreshError);
                }
            } else {
                sessionStorage.clear();
                window.location.href = '/login';
            }
        }
        return Promise.reject(error);
    }
);

export default axiosInstance;