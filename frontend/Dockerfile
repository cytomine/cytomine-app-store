ARG APP_STORE_VERSION
ARG APP_STORE_REVISION
ARG NGINX_VERSION=1.22.1
ARG NODE_VERSION=20.19.4
ARG PROXY_CACHE=registry.cytomine.org/docker

# Build stage
FROM ${PROXY_CACHE}/node:${NODE_VERSION}-alpine AS build-stage

WORKDIR /app

COPY ./package.json /app/package.json
COPY ./package-lock.json /app/package-lock.json
RUN npm install

COPY . .

RUN npm run build

# Development stage
FROM ${PROXY_CACHE}/node:${NODE_VERSION}-bookworm-slim AS dev-server

WORKDIR /app

COPY package.json .

RUN npm install

CMD ["npm", "run", "dev"]

# Production stage
FROM ${PROXY_CACHE}/nginx:${NGINX_VERSION}-alpine AS prod-server

ARG APP_STORE_REVISION
ARG APP_STORE_VERSION
ARG NODE_VERSION

LABEL org.opencontainers.image.authors="uliege@cytomine.org" \
      org.opencontainers.image.url="https://uliege.cytomine.org/" \
      org.opencontainers.image.documentation="https://doc.uliege.cytomine.org/" \
      org.opencontainers.image.source="https://github.com/cytomine/Cytomine-app-store" \
      org.opencontainers.image.vendor="Cytomine ULiège" \
      org.opencontainers.image.version=${APP_STORE_VERSION} \
      org.opencontainers.image.revision=${APP_STORE_REVISION} \
      org.opencontainers.image.deps.nginx.version=${NGINX_VERSION} \
      org.opencontainers.image.deps.node.version=${NODE_VERSION}

COPY --from=build-stage /app/dist /usr/share/nginx/html

COPY nginx.conf  /etc/nginx

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
