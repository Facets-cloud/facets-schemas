FROM node:12.18-alpine as builder
WORKDIR /app
ENV PATH /app/node_modules/.bin:$PATH
COPY package.json /app/
RUN apk add git
RUN npm install
RUN npm rebuild node-sass
COPY . /app/
RUN /app/node_modules/@angular/cli/bin/ng build --prod

FROM nginx:alpine
RUN rm -rf /usr/share/nginx/html/*
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=builder /app/dist/capc /usr/share/nginx/html/capc
EXPOSE 3200
CMD ["nginx", "-g", "daemon off;"]
