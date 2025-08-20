import client from './client';

import type { App } from '@/types/types';

export const getAllTasks = async () => {
  const response = await client.get('/tasks');
  return response.data;
};

export const getTask = async (namespace: string, version: string): Promise<App> => {
  if (!namespace || !version) {
    throw new Error('Namespace and version are required');
  }

  try {
    const response = await client.get<App>(`/tasks/${namespace}/${version}`);
    return response.data;
  } catch (error) {
    console.error(`Failed to fetch task ${namespace}/${version}:`, error);
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
