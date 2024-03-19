FROM node:lts-alpine as build

WORKDIR /client

COPY package.json ./

RUN npm install -g @angular/cli
RUN npm install

COPY . .

RUN ng build

FROM nginx:alpine

RUN rm -rf /usr/share/nginx/html/*

COPY --from=build /client/dist/client/browser /usr/share/nginx/html

EXPOSE 80