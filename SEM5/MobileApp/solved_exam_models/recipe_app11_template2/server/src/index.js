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

const recipes = [
  {id: 1, name: "r1", details: "perisoare", time: 20, type: "soup", rating: 6 },
  {id: 2, name: "r2", details: "taitei", time: 30, type: "soup", rating: 9 },
  {id: 3, name: "r3", details: "ciorba", time: 25, type: "soup", rating: 7 },
  {id: 4, name: "r4", details: "pui", time: 50, type: "meat", rating: 10 },
  {id: 5, name: "r5", details: "porc", time: 70, type: "meat", rating: 9 },
  {id: 6, name: "r6", details: "vita", time: 50, type: "meat", rating: 10 },
  {id: 7, name: "r7", details: "curcan", time: 40, type: "meat", rating: 7 },
  {id: 8, name: "r8", details: "legume", time: 20, type: "vegan", rating: 7 },
  {id: 9, name: "r9", details: "soia", time: 15, type: "vegan", rating: 8 },
  ];


const router = new Router();

router.get('/types', ctx => {
  const types = recipes.map(recipe => recipe.type);
  const uniqueTypes = new Set(types);
  ctx.response.body = [...uniqueTypes];
  ctx.response.status = 200;
});

router.get('/recipes/:type', ctx => {
  // console.log("ctx: " + JSON.stringify(ctx));
  const headers = ctx.params;
  const type = headers.type;
  // console.log("category: " + JSON.stringify(category));
  ctx.response.body = recipes.filter(recipe => recipe.type == type);
  // console.log("body: " + JSON.stringify(ctx.response.body));
  ctx.response.status = 200;
});

router.get('/recipes', ctx => {
  ctx.response.body = recipes;
  ctx.response.status = 200;
});

router.get('/low', ctx => {
  const underratedRecipes = [...recipes];
  underratedRecipes.sort((a, b) => a.rating - b.rating);
  const topUnderrated = underratedRecipes.slice(0, 10);
  ctx.response.body = topUnderrated;
  //console.log(topUnderrated);
  ctx.response.status = 200;
});

router.post('/increment', ctx => {
  const { id } = ctx.request.body;

  const recipe = recipes.find(r => r.id === id);
  if (!recipe) {
    ctx.response.status = 404;
    ctx.response.body = { message: "Recipe not found" };
    return;
  }

  recipe.rating += 1;

  ctx.response.status = 200;
  ctx.response.body = {
    message: "Rating updated successfully",
    updatedRecipe: recipe,
  };
});


const broadcast = (data) =>
  wss.clients.forEach((client) => {
    if (client.readyState === WebSocket.OPEN) {
      client.send(JSON.stringify(data));
    }
  });

router.post('/recipe', ctx => {
  // console.log("ctx: " + JSON.stringify(ctx));
  const headers = ctx.request.body;
  // console.log("body: " + JSON.stringify(headers));
  const name = headers.name;
  const details = headers.details;
  const time = headers.time;
  const type = headers.type;
  const rating = headers.rating;
  if (typeof name !== 'undefined'
    && typeof details !== 'undefined'
    && typeof time !== 'undefined'
    && typeof type !== 'undefined'
    && typeof rating !== 'undefined') {
    const index = recipes.findIndex(recipe => recipe.type == type && recipe.name == name);
    if (index !== -1) {
      const msg = "Recipe name already exists!";
      console.log(msg);
      ctx.response.body = { text: msg };
      ctx.response.status = 404;
    } else {
      let maxId = Math.max.apply(Math, recipes.map(recipe => recipe.id)) + 1;
      let recipe = {
        id: maxId,
        name: name,
        details,
        time,
        type,
        rating
      };
      recipes.push(recipe);
      broadcast(recipe);
      ctx.response.body = recipe;
      ctx.response.status = 200;
    }
  } else {
    const msg = "Missing or invalid name: " + name + " details: " + details
      + " time: " + time + " type: " + type + " rating: " + rating;
    console.log(msg);
    ctx.response.body = { text: msg };
    ctx.response.status = 404;
  }
});

router.del('/recipe/:id', ctx => {
  // console.log("ctx: " + JSON.stringify(ctx));
  const headers = ctx.params;
  // console.log("body: " + JSON.stringify(headers));
  const id = headers.id;
  if (typeof id !== 'undefined') {
    const index = recipes.findIndex(recipe => recipe.id == id);
    if (index === -1) {
      const msg = "No recipe with id: " + id;
      console.log(msg);
      ctx.response.body = { text: msg };
      ctx.response.status = 404;
    } else {
      let recipe = recipes[index];
      recipes.splice(index, 1);
      ctx.response.body = recipe;
      ctx.response.status = 200;
    }
  } else {
    ctx.response.body = { text: 'Id missing or invalid' };
    ctx.response.status = 404;
  }
});

app.use(router.routes());
app.use(router.allowedMethods());

const port = 2307;

server.listen(port, () => {
  console.log(`🚀 Server listening on ${port} ... 🚀`);
});