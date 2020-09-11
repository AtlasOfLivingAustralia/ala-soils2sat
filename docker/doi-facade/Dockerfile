FROM node:14-buster

ADD . /app
WORKDIR /app
RUN yarn install --frozen-lockfile

EXPOSE 3000

CMD [ "yarn", "start:prod" ]
