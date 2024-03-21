FROM node:lts-alpine as build
WORKDIR /client
COPY package.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
RUN rm -rf /usr/share/nginx/html/*
COPY default.conf /etc/nginx/conf.d/
COPY --from=build /client/dist/client/browser /usr/share/nginx/html
EXPOSE 80