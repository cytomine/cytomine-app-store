export interface App {
  id: string;
  name: string;
  namespace: string;
  version: string;
  date: string;
  description?: string;
  imageUrl?: string;
};

export interface Search {
  name: string;
  namespace: string;
  nameshort: string;
  imageName: string;
  version: string;
  description?: string;
};
