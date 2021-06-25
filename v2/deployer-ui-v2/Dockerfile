FROM node:12.18-alpine as builder
WORKDIR /app
ENV PATH /app/node_modules/.bin:$PATH
COPY . /app/
RUN apk add git
RUN npm install @angular/cli@8.3.25
RUN npm install
RUN npm rebuild node-sass
RUN /app/node_modules/@angular/cli/bin/ng build --prod

FROM nginx:alpine
RUN rm -rf /usr/share/nginx/html/*
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=builder /app/dist /usr/share/nginx/html
EXPOSE 3100
CMD ["nginx", "-g", "daemon off;"]
