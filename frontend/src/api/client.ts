import axios from 'axios';

const client = axios.create({
  baseURL: '/api/v1',
  timeout: 10000,
});

export default client;