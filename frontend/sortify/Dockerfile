FROM node:20-alpine

WORKDIR /app

COPY package.json package-lock.json

RUN npm install -g npm@latest 
RUN npm i @tailwindcss/oxide-linux-arm64-musl

COPY . .

RUN npm run build

EXPOSE 3000

CMD ["npm", "run", "start"]