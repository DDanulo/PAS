import axios from 'axios';

const axiosSetup = axios.create({
    baseURL: 'http://10.128.156.134:8080/PAS_PD-1/api/v1',
    headers: {
        'Content-Type': 'application/json',
    },
});

export default axiosSetup;