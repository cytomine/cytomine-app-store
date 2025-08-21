export interface App {
  id: string;
  name: string;
  namespace: string;
  version: string;
  date: string;
  description?: string;
  imageUrl?: string;
  authors: Author[];
};

export interface Author {
  firstName: string;
  lastName: string;
  organization: string;
  email: string;
  isContact: boolean;
};

export interface Search {
  name: string;
  namespace: string;
  nameshort: string;
  imageName: string;
  version: string;
  description?: string;
};
