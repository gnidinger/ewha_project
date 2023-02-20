import axios from 'axios';
import { getCookie } from './cookie';

export const axiosApi = axios.create({
  // baseURL: process.env.REACT_APP_API_URL,
  headers: {
    authorization: `Bearer ${getCookie('ari_login')}`
  },
  withCredentials: true
});

export default axiosApi;