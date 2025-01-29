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

var tasks = [
  { id: 1, date: "2022-01-01", type: "Work", duration: 2.5, priority: "High", category: "Project", description: "Work on Project A" },
  { id: 2, date: "2022-01-02", type: "Study", duration: 1.5, priority: "Medium", category: "Learning", description: "Read a book on JavaScript" },
  { id: 3, date: "2022-01-03", type: "Exercise", duration: 1, priority: "Low", category: "Health", description: "Go for a jog" },
  { id: 4, date: "2022-01-01", type: "Work", duration: 3, priority: "High", category: "Project", description: "Attend project meeting" },
  { id: 5, date: "2022-01-02", type: "Entertainment", duration: 2, priority: "Medium", category: "Leisure", description: "Watch a movie" },
  { id: 6, date: "2022-01-03", type: "Study", duration: 2, priority: "Medium", category: "Learning", description: "Complete online course" },
  { id: 7, date: "2022-01-01", type: "Exercise", duration: 1.5, priority: "Low", category: "Health", description: "Gym workout" },
  { id: 8, date: "2022-01-02", type: "Work", duration: 4, priority: "High", category: "Project", description: "Code review" },
  { id: 9, date: "2022-01-03", type: "Entertainment", duration: 1.5, priority: "Medium", category: "Leisure", description: "Play video games" },
  { id: 10, date: "2022-02-10", type: "Study", duration: 1.5, priority: "Medium", category: "Learning", description: "Practice coding challenges" },
  { id: 11, date: "2022-02-11", type: "Exercise", duration: 1, priority: "Low", category: "Health", description: "Morning yoga" },
  { id: 12, date: "2022-02-12", type: "Work", duration: 3, priority: "High", category: "Project", description: "Client meeting" },
  { id: 13, date: "2022-02-10", type: "Entertainment", duration: 2.5, priority: "Medium", category: "Leisure", description: "Listen to music" },
  { id: 14, date: "2022-02-11", type: "Study", duration: 2, priority: "Medium", category: "Learning", description: "Read articles on technology" },
  { id: 15, date: "2022-02-12", type: "Exercise", duration: 1.5, priority: "Low", category: "Health", description: "Evening run" }
];

const router = new Router();
router.get('/taskDays', ctx => {
  const dates = tasks.map(task => task.date);
  const uniqueDates = new Set(dates);
  ctx.response.body = [...uniqueDates];
  ctx.response.status = 200;
});

router.get('/details/:date', ctx => {
  // console.log("ctx: " + JSON.stringify(ctx));
  const headers = ctx.params;
  const date = headers.date;
  // console.log("category: " + JSON.stringify(category));
  ctx.response.body = tasks.filter(task => task.date == date);
  // console.log("body: " + JSON.stringify(ctx.response.body));
  ctx.response.status = 200;
});

router.get('/entries', ctx => {
  ctx.response.body = tasks;
  ctx.response.status = 200;
});

const broadcast = (data) =>
  wss.clients.forEach((client) => {
    if (client.readyState === WebSocket.OPEN) {
      client.send(JSON.stringify(data));
    }
  });

router.post('/task', ctx => {
  // console.log("ctx: " + JSON.stringify(ctx));
  const headers = ctx.request.body;
  // console.log("body: " + JSON.stringify(headers));
  const date = headers.date;
  const type = headers.type;
  const duration = headers.duration;
  const priority = headers.priority;
  const category = headers.category;
  const description = headers.description;
  if (typeof date !== 'undefined'
    && typeof type !== 'undefined'
    && typeof duration !== 'undefined'
    && typeof priority !== 'undefined'
    && typeof category !== 'undefined'
    && typeof description !== 'undefined') {
    const index = tasks.findIndex(task => task.date == date && task.type == type);
    if (index !== -1) {
      const msg = "The entity already exists!";
      console.log(msg);
      ctx.response.body = { text: msg };
      ctx.response.status = 404;
    } else {
      let maxId = Math.max.apply(Math, tasks.map(task => task.id)) + 1;
      let task = {
        id: maxId,
        date,
        type,
        duration,
        priority,
        category,
        description
      };
      tasks.push(task);
      broadcast(task);
      ctx.response.body = task;
      ctx.response.status = 200;
    }
  } else {
    const msg = "Missing or invalid date: " + date + " type: " + type + " duration: " + duration
      + " priority: " + priority + " category: " + category + " description: " + description;
    console.log(msg);
    ctx.response.body = { text: msg };
    ctx.response.status = 404;
  }
});

router.del('/task/:id', ctx => {
  // console.log("ctx: " + JSON.stringify(ctx));
  const headers = ctx.params;
  // console.log("body: " + JSON.stringify(headers));
  const id = headers.id;
  if (typeof id !== 'undefined') {
    const index = tasks.findIndex(task => task.id == id);
    if (index === -1) {
      const msg = "No entity with id: " + id;
      console.log(msg);
      ctx.response.body = { text: msg };
      ctx.response.status = 404;
    } else {
      let task = tasks[index];
      tasks.splice(index, 1);
      ctx.response.body = task;
      ctx.response.status = 200;
    }
  } else {
    ctx.response.body = { text: 'Id missing or invalid' };
    ctx.response.status = 404;
  }
});

app.use(router.routes());
app.use(router.allowedMethods());

const port = 2404;

server.listen(port, () => {
  console.log(`🚀 Server listening on ${port} ... 🚀`);
});