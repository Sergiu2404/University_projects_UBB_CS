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

const getRandomInt = (min, max) => {
  min = Math.ceil(min);
  max = Math.floor(max);
  return Math.floor(Math.random() * (max - min)) + min;
};

const tipNames = [
  "Green Thumbs Up",
  "Sprout Squad",
  "Nature Nurturers",
  "Blooming Bunch",
  "Garden Gang",
  "Root Rookies",
  "Grow Greatness",
  "Plant Posse",
  "Blossoming Brigade",
  "Soil Sages"
];

const descriptionNames = [
  "Choose the right soil for your plants.",
  "Plant at the proper depth for optimal growth.",
  "Water your plants at the base to avoid foliage disease.",
  "Fertilize regularly for lush foliage and abundant blooms.",
  "Prune regularly to encourage healthy growth and shape.",
  "Mulch to retain moisture and suppress weeds.",
  "Provide proper light and air circulation for your plants.",
  "Control pests and diseases before they become a problem.",
  "Rotate your crops to reduce soil-borne diseases.",
  "Experiment with companion planting for a diverse and healthy garden."
];

const materialNames = [
  "Compost",
  "Potting Soil",
  "Fertilizer",
  "Mulch",
  "Pruning Shears",
  "Garden Hose",
  "Sprinkler",
  "Gardening Gloves",
  "Garden Shovel",
  "Pest Repellent"
];

const stepNames = [
  "Prepare the soil by tilling and removing any weeds or debris.",
  "Choose the right plants for your growing conditions, such as light and soil type.",
  "Plant the seeds or seedlings at the recommended depth and spacing.",
  "Water the plants thoroughly after planting, and continue to water regularly.",
  "Fertilize the plants according to the recommended schedule.",
  "Mulch around the plants to retain moisture and suppress weeds.",
  "Prune the plants as needed to encourage healthy growth and shape.",
  "Control pests and diseases with natural or organic methods.",
  "Monitor the plants regularly for signs of stress or problems.",
  "Harvest the fruits and vegetables when they are ripe and at their peak."
];

const categories = [
  "Soil Preparation",
  "Plant Selection",
  "Planting",
  "Watering",
  "Fertilizing",
  "Mulching",
  "Pruning",
  "Pest Control",
  "Monitoring",
  "Harvesting"
];

const dificulties = ['easy', 'medium', 'hard'];
const tips = [];
for (let i = 0; i < 20; i++) {
  tips.push({
    id: i + 1,
    name: tipNames[getRandomInt(0, tipNames.length)],
    description: descriptionNames[getRandomInt(0, descriptionNames.length)],
    materials: materialNames[getRandomInt(0, materialNames.length)],
    steps: stepNames[getRandomInt(0, stepNames.length)],
    category: categories[getRandomInt(0, categories.length)],
    difficulty: dificulties[getRandomInt(0, dificulties.length)]
  });
}

const router = new Router();
router.get('/categories', ctx => {
  ctx.response.body = categories;
  ctx.response.status = 200;
});

router.get('/tips/:category', ctx => {
  // console.log("ctx: " + JSON.stringify(ctx));
  const headers = ctx.params;
  const category = headers.category;
  // console.log("category: " + JSON.stringify(category));
  ctx.response.body = tips.filter(tip => tip.category == category);
  // console.log("body: " + JSON.stringify(ctx.response.body));
  ctx.response.status = 200;
});

router.get('/easiest', ctx => {
  ctx.response.body = tips;
  ctx.response.status = 200;
});

router.post('/difficulty', ctx => {
  // console.log("ctx: " + JSON.stringify(ctx));
  const headers = ctx.request.body;
  const difficulty = headers.difficulty;
  // console.log("body: " + JSON.stringify(headers));
  const id = headers.id;
  if (typeof id !== 'undefined' && typeof difficulty !== 'undefined') {
    const index = tips.findIndex(tip => tip.id == id);
    if (index === -1) {
      const msg = "Tip not available!";
      console.log(msg);
      ctx.response.body = { text: msg };
      ctx.response.status = 404;
    } else {
      let tip = tips[index];
      tip.difficulty = difficulty;
      ctx.response.body = tip;
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

router.post('/tip', ctx => {
  // console.log("ctx: " + JSON.stringify(ctx));
  const headers = ctx.request.body;
  // console.log("body: " + JSON.stringify(headers));
  const name = headers.name;
  const description = headers.description;
  const materials = headers.materials;
  const steps = headers.steps;
  const category = headers.category;
  const difficulty = headers.difficulty;
  if (typeof name !== 'undefined'
    && typeof description !== 'undefined'
    && typeof materials !== 'undefined'
    && typeof steps !== 'undefined'
    && typeof category !== 'undefined'
    && typeof difficulty !== 'undefined') {
    const index = tips.findIndex(tip => tip.name == name);
    if (index !== -1) {
      const msg = "Tip already exists!";
      console.log(msg);
      ctx.response.body = { text: msg };
      ctx.response.status = 404;
    } else {
      let maxId = Math.max.apply(Math, tips.map(function (tip) {
        return tip.id;
      })) + 1;
      let tip = {
        id: maxId,
        name,
        description,
        materials,
        steps,
        category,
        difficulty
      };
      tips.push(tip);
      broadcast(tip);
      ctx.response.body = tip;
      ctx.response.status = 200;
    }
  } else {
    const message = "Missing or invalid: name, description, materials, steps, category or difficulty!";
    console.log(message);
    ctx.response.body = { text: message };
    ctx.response.status = 404;
  }
});

router.del('/tip/:id', ctx => {
  // console.log("ctx: " + JSON.stringify(ctx));
  const headers = ctx.params;
  // console.log("body: " + JSON.stringify(headers));
  const id = headers.id;
  if (typeof id !== 'undefined') {
    const index = tips.findIndex(tip => tip.id == id);
    if (index === -1) {
      const msg = "No tip with id: " + id;
      console.log(msg);
      ctx.response.body = { text: msg };
      ctx.response.status = 404;
    } else {
      let tip = tips[index];
      tips.splice(index, 1);
      ctx.response.body = tip;
      ctx.response.status = 200;
    }
  } else {
    ctx.response.body = { text: 'Id missing or invalid' };
    ctx.response.status = 404;
  }
});

app.use(router.routes());
app.use(router.allowedMethods());

const port = 2302;

server.listen(port, () => {
  console.log(`🚀 Server listening on ${port} ... 🚀`);
});