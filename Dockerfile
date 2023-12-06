FROM node:16-alpine as builder

WORKDIR /app

COPY package.json .
RUN npm install

COPY . .

RUN npm run build

FROM node:16-alpine

WORKDIR /app

COPY --from=builder /app/build .

EXPOSE 3000

CMD ["npm", "start"]
