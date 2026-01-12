import axios from 'axios';

const axiosSetup = axios.create({
    baseURL: 'http://localhost:8080/PAS_SPA/api/v1',
    headers: {
        'Content-Type': 'application/json',
    },
});

export default axiosSetup;