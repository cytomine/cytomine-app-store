import { getTaskBundle } from '@/api/tasks';

export function useTask() {
  const downloadTask = async (namespace: string, version: string) => {
    try {
      const blobData = await getTaskBundle(namespace, version);
      const blob = new Blob([blobData], { type: 'application/zip' });
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = `${namespace}-${version}.zip`;
      link.click();
      URL.revokeObjectURL(link.href);
    } catch (error) {
      console.error('Download failed:', error);
      alert('Failed to download file.');
    }
  };

  return { downloadTask };
}