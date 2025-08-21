import client from './client';

export const getAllTasks = async () => {
  const response = await client.get('/tasks');
  return response.data;
};

export const getTaskBundle = async (namespace: string, version: string) => {
  if (!namespace || !version) {
    throw new Error('Namespace and version are required');
  }

  try {
    const response = await client.get(
      `/tasks/${namespace}/${version}/bundle.zip`,
      { responseType: 'arraybuffer' },
    );
    return response.data;
  } catch (error) {
    console.error(`Failed to download task ${namespace}/${version}:`, error);
    throw error;
  }
};

export const createTask = async (data: FormData) => {
  const response = await client.post('/tasks', data);
  return response.data;
};

export const searchTasks = async (query: string) => {
  const response = await client.get('/tasks/search', { params: { query } });
  return response.data;
};
