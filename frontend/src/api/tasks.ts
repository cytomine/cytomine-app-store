import client from './client';

export const getAllTasks = async () => {
  const response = await client.get('/tasks');
  return response.data;
};

export const createTask = async (data: FormData) => {
  const response = await client.post('/tasks', data);
  return response.data;
};

export const searchTasks = async (query: string) => {
  const response = await client.get('/tasks/search', { params: { query } });
  return response.data;
};
