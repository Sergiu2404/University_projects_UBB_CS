var koa = require('koa');
var app = module.exports = new koa();
const server = require('http').createServer(app.callback());
const WebSocket = require('ws');
const wss = new WebSocket.Server({ server });
const Router = require('koa-router');
const cors = require('@koa/cors');
const bodyParser = require('koa-bodyparser');

app.use(bodyParser());

app.use(cors());

app.use(middleware);

function middleware(ctx, next) {
  const start = new Date();
  return next().then(() => {
    const ms = new Date() - start;
    console.log(`${start.toLocaleTimeString()} ${ctx.response.status} ${ctx.request.method} ${ctx.request.url} - ${ms}ms`);
  });
}

const files = [
  { id: 1, name: "File1", status: "open", size: 1000, location: "root", usage: 350 },
  { id: 2, name: "File2", status: "open", size: 1000, location: "src", usage: 150 },
  { id: 3, name: "FIle3", status: "open", size: 1000, location: "root", usage: 80 },
  { id: 4, name: "FIle4", status: "open", size: 1000, location: "user", usage: 120 },
  { id: 5, name: "FIle5", status: "open", size:1000, location: "root", usage: 800 },
  { id: 6, name: "FIle6", status: "open", size: 1000, location: "src", usage: 250 },
  { id: 7, name: "FIle7", status: "shared", size: 1000, location: "src", usage: 180 },
  { id: 8, name: "FIle8", status: "open", size: 1000, location: "user", usage: 40 },
  { id: 9, name: "FIle9", status: "shared", size: 1000, location: "system", usage: 90 },
  { id: 10, name: "FIle10", status: "shared", size: 1000, location: "root", usage: 130 },
  { id: 11, name: "FIle11", status: "shared", size: 1000, location: "user", usage: 280 },
  { id: 12, name: "FIle12", status: "shared", size: 1000, location: "home", usage: 200 },
  { id: 13, name: "FIle13", status: "shared", size: 1000, location: "user", usage: 60 },
  { id: 14, name: "FIle14", status: "shared", size: 1000, location: "root", usage: 180 },
  { id: 15, name: "FIle15", status: "shared", size: 1000, location: "home", usage: 120 },
];

const router = new Router();

router.get('/all', ctx => {
    //console.log(ctx.request);
  ctx.response.body = files;
  ctx.response.status = 200;
});

router.get('/locations', ctx => {
  const allLocations = files.map(file => file.location);
  const locationsSet = new Set(allLocations);
  ctx.response.body = [...locationsSet];
  ctx.response.status = 200;
});

router.get('/files/:location', ctx => {
    const headers = ctx.params;
    const location = headers.location;

    const filesByLocation = files.filter(file => file.location == location);
    console.log(filesByLocation);

    ctx.response.body = [...filesByLocation];
    ctx.response.status = 200;
  });

const broadcast = (data) =>
  wss.clients.forEach((client) => {
    if (client.readyState === WebSocket.OPEN) {
      client.send(JSON.stringify(data));
    }
  });

router.post('/file', ctx => {
  const headers = ctx.request.body;

  const name = headers.name;
  const status = headers.status;
  const size = headers.size;
  const location = headers.location;
  const usage = headers.usage;

  console.log(name, status, size, location, usage);
  if (typeof name !== 'undefined'
    && typeof status !== 'undefined'
    && typeof size !== 'undefined'
    && typeof location !== 'undefined'
    && typeof usage !== 'undefined') {
    const index = files.findIndex(file => file.name == name && file.status == status);
    if (index !== -1) {
      const msg = "The entity already exists!";
      console.log(msg);
      ctx.response.body = { text: msg };
      ctx.response.status = 404;
    } else {
      let maxId = Math.max.apply(Math, files.map(file => file.id)) + 1;
      let newFile = {
        id: maxId,
        name,
        status,
        size,
        location,
        usage
      };
      files.push(newFile);
      broadcast(newFile);
      ctx.response.body = newFile;
      ctx.response.status = 200;
    }
  } else {
    const msg = "Missing or invalid name: " + name + " status: " + status + " size: " + size
      + " location: " + location + " usage: " + usage;
    console.log(msg);
    ctx.response.body = { text: msg };
    ctx.response.status = 404;
  }

  router.del("/file/:id", (ctx) => {
    // console.log("ctx: " + JSON.stringify(ctx));
    const headers = ctx.params;
    // console.log("body: " + JSON.stringify(headers));
    const id = headers.id;
    if (typeof id !== "undefined") {
      const index = files.findIndex((file) => file.id == id);
      if (index === -1) {
        const msg = "No entity with id: " + id;
        console.log(msg);
        ctx.response.body = { text: msg };
        ctx.response.status = 404;
      } else {
        let file = files[index];
        files.splice(index, 1);
        ctx.response.body = file;
        ctx.response.status = 200;
      }
    } else {
      ctx.response.body = { text: "Id missing or invalid" };
      ctx.response.status = 404;
    }
  });
  



});

app.use(router.routes());
app.use(router.allowedMethods());

const port = 2404;

server.listen(port, '0.0.0.0', () => {
  console.log(`🚀 Server listening on ${port} ... 🚀`);
});