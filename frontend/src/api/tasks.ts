import client from './client';

export const getAllTasks = async () => {
  const response = await client.get('/tasks');
  return response.data;
};
