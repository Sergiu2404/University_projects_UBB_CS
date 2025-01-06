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
    console.log(`${start.toLocaleTimeString()} ${ctx.response.status}  ${ctx.request.method} ${ctx.request.url} - ${ms}ms`);
  });
}

const getRandomInt = (min, max) => {
  min = Math.ceil(min);
  max = Math.floor(max);
  return Math.floor(Math.random() * (max - min)) + min;
};

const receipeNames = [
  'Healthier chicken chow mein',
  'One-pot healthy Mexican beef mince',
  'Mongolian chicken',
  'One-pan butter chicken with rice',
  '17-minute chicken pot pie',
  'Spanish baked chicken with chorizo rice'];
const descriptionNames = ['very good', 'healthy', 'lite', 'tasty', 'good'];
const ingredientsNames = ['chicken', 'rice', 'salamy', 'bacon', 'eggs'];
const instructionsNames = [
  '1. prepare, 2. eat',
  '1. prepare, 2. bake, 3. enjoy',
  '1. prepare, 2. order delivey'];
const categories = ['desserts', 'main dishes', 'vegan', 'keto'];
const dificulties = ['easy', 'medium', 'hard'];
const recipes = [];
for (let i = 0; i < 20; i++) {
  recipes.push({
    id: i + 1,
    name: receipeNames[getRandomInt(0, receipeNames.length)],
    description: descriptionNames[getRandomInt(0, descriptionNames.length)],
    ingredients: ingredientsNames[getRandomInt(0, ingredientsNames.length)],
    instructions: instructionsNames[getRandomInt(0, instructionsNames.length)],
    category: categories[getRandomInt(0, categories.length)],
    difficulty: dificulties[getRandomInt(0, dificulties.length)]
  });
}

const router = new Router();
router.get('/categories', ctx => {
  ctx.response.body = categories;
  ctx.response.status = 200;
});

router.get('/recipes/:category', ctx => {
  // console.log("ctx: " + JSON.stringify(ctx));
  const headers = ctx.params;
  const category = headers.category;
  // console.log("category: " + JSON.stringify(category));
  ctx.response.body = recipes.filter(recipe => recipe.category == category);
  // console.log("body: " + JSON.stringify(ctx.response.body));
  ctx.response.status = 200;
});

router.get('/easiest', ctx => {
  ctx.response.body = recipes;
  ctx.response.status = 200;
});

router.post('/difficulty', ctx => {
  // console.log("ctx: " + JSON.stringify(ctx));
  const headers = ctx.request.body;
  const difficulty = headers.difficulty;
  // console.log("body: " + JSON.stringify(headers));
  const id = headers.id;
  if (typeof id !== 'undefined' && typeof difficulty !== 'undefined') {
    const index = recipes.findIndex(recipe => recipe.id == id);
    if (index === -1) {
      console.log("Recipe not available!");
      ctx.response.body = { text: 'Recipe not available!' };
      ctx.response.status = 404;
    } else {
      let recipe = recipes[index];
      recipe.difficulty = difficulty;
      ctx.response.body = recipe;
      ctx.response.status = 200;
    }
  } else {
    console.log("Missing or invalid: id or difficulty!");
    ctx.response.body = { text: 'Missing or invalid: id or difficulty!' };
    ctx.response.status = 404;
  }
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
  const description = headers.description;
  const ingredients = headers.ingredients;
  const instructions = headers.instructions;
  const category = headers.category;
  const difficulty = headers.difficulty;
  if (typeof name !== 'undefined'
    && typeof description !== 'undefined'
    && typeof ingredients !== 'undefined'
    && typeof instructions !== 'undefined'
    && typeof category !== 'undefined'
    && typeof difficulty !== 'undefined') {
    const index = recipes.findIndex(recipe => recipe.name == name);
    if (index !== -1) {
      console.log("Recipe already exists!");
      ctx.response.body = { text: 'Recipe already exists!' };
      ctx.response.status = 404;
    } else {
      let maxId = Math.max.apply(Math, recipes.map(function (recipe) {
        return recipe.id;
      })) + 1;
      let recipe = {
        id: maxId,
        name,
        description,
        ingredients,
        instructions,
        category,
        difficulty
      };
      recipes.push(recipe);
      broadcast(recipe);
      ctx.response.body = recipe;
      ctx.response.status = 200;
    }
  } else {
    const message = "Missing or invalid: name, description, ingredients, instructions, category or difficulty!";
    console.log(message);
    ctx.response.body = { text: message };
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
      console.log("No recipe with id: " + id);
      ctx.response.body = { text: 'Invalid recipe id' };
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

const port = 2325;

server.listen(port, () => {
  console.log(`🚀 Server listening on ${port} ... 🚀`);
});