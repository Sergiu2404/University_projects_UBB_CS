const koa = require('koa');
const app = module.exports = new koa();
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
  { id: 1, date: '2025-01-15', title: 'Spaghetti Carbonara', ingredients: 'Spaghetti, Eggs, Bacon, Parmesan', category: 'Main Course', rating: 4.5 },
  { id: 2, date: '2025-01-16', title: 'Chocolate Cake', ingredients: 'Flour, Cocoa Powder, Eggs, Sugar, Butter', category: 'Dessert', rating: 4.8 },
  { id: 3, date: '2025-01-17', title: 'Caesar Salad', ingredients: 'Romaine Lettuce, Croutons, Parmesan, Caesar Dressing', category: 'Appetizer', rating: 4.2 },
  { id: 4, date: '2025-01-18', title: 'Grilled Salmon', ingredients: 'Salmon, Lemon, Garlic, Olive Oil', category: 'Main Course', rating: 4.7 },
  { id: 5, date: '2025-01-19', title: 'Banana Smoothie', ingredients: 'Banana, Milk, Honey, Ice', category: 'Beverage', rating: 4.3 },
  { id: 6, date: '2025-02-20', title: 'Tomato Soup', ingredients: 'Tomatoes, Onion, Garlic, Basil, Cream', category: 'Appetizer', rating: 4.6 },
  { id: 7, date: '2025-02-21', title: 'Beef Stroganoff', ingredients: 'Beef, Mushrooms, Onion, Sour Cream, Pasta', category: 'Main Course', rating: 4.4 },
  { id: 8, date: '2025-02-22', title: 'Cheesecake', ingredients: 'Cream Cheese, Sugar, Eggs, Graham Crackers', category: 'Dessert', rating: 4.9 },
  { id: 9, date: '2025-02-23', title: 'Margarita Pizza', ingredients: 'Pizza Dough, Tomato Sauce, Mozzarella, Basil', category: 'Main Course', rating: 4.6 },
  { id: 10, date: '2025-02-24', title: 'Greek Salad', ingredients: 'Cucumber, Tomato, Feta Cheese, Olives, Olive Oil', category: 'Appetizer', rating: 4.3 }
];

const router = new Router();

router.get('/recipes', ctx => {
  ctx.response.body = recipes;
  ctx.response.status = 200;
});

router.get('/recipe/:id', ctx => {
  const { id } = ctx.params;
  const recipe = recipes.find(r => r.id == id);
  if (recipe) {
    ctx.response.body = recipe;
    ctx.response.status = 200;
  } else {
    ctx.response.body = { error: `Recipe with id ${id} not found` };
    ctx.response.status = 404;
  }
});

router.post('/recipe', ctx => {
  const { date, title, ingredients, category, rating } = ctx.request.body;

  if (date && title && ingredients && category && rating) {
    const id = recipes.length > 0 ? Math.max(...recipes.map(r => r.id)) + 1 : 1;
    const newRecipe = { id, date, title, ingredients, category, rating };
    recipes.push(newRecipe);

    broadcast(newRecipe);
    ctx.response.body = newRecipe;
    ctx.response.status = 201;
  } else {
    ctx.response.body = { error: "Missing or invalid recipe details" };
    ctx.response.status = 400;
  }
});

router.del('/recipe/:id', ctx => {
  const { id } = ctx.params;
  const index = recipes.findIndex(r => r.id == id);
  if (index !== -1) {
    const removedRecipe = recipes.splice(index, 1)[0];
    ctx.response.body = removedRecipe;
    ctx.response.status = 200;
  } else {
    ctx.response.body = { error: `Recipe with id ${id} not found` };
    ctx.response.status = 404;
  }
});

router.get('/allRecipes', ctx => {
  ctx.response.body = recipes;
  ctx.response.status = 200;
});

const broadcast = (data) => {
  wss.clients.forEach((client) => {
    if (client.readyState === WebSocket.OPEN) {
      client.send(JSON.stringify(data));
    }
  });
};

app.use(router.routes());
app.use(router.allowedMethods());

const port = 2404;

server.listen(port, () => {
  console.log(`🚀 Server listening on ${port} ... 🚀`);
});
