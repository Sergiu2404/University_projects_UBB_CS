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

const projects = [
  { id: 1, name: "Project A", team: "Development Team", details: "Web application development", status: "in progress", members: 8, type: "software development" },
  { id: 2, name: "Project B", team: "Marketing Team", details: "Product launch campaign", status: "planning", members: 5, type: "marketing" },
  { id: 3, name: "Project C", team: "Design Team", details: "UI/UX redesign", status: "completed", members: 6, type: "design" },
  { id: 4, name: "Project D", team: "Research Team", details: "Market analysis", status: "on hold", members: 4, type: "research" },
  { id: 5, name: "Project E", team: "QA Team", details: "Testing phase", status: "in progress", members: 7, type: "quality assurance" },
  { id: 6, name: "Project F", team: "Sales Team", details: "Client outreach", status: "planning", members: 3, type: "sales" },
  { id: 7, name: "Project G", team: "Support Team", details: "Customer support improvement", status: "completed", members: 5, type: "support" },
  { id: 8, name: "Project H", team: "Finance Team", details: "Budget planning", status: "in progress", members: 6, type: "finance" },
  { id: 9, name: "Project I", team: "HR Team", details: "Employee training program", status: "planning", members: 4, type: "human resources" },
  { id: 10, name: "Project J", team: "Event Team", details: "Annual company event planning", status: "completed", members: 8, type: "event planning" },
  { id: 11, name: "Project K", team: "IT Team", details: "Infrastructure upgrade", status: "in progress", members: 10, type: "information technology" },
  { id: 12, name: "Project L", team: "Legal Team", details: "Contract review", status: "planning", members: 5, type: "legal" },
  { id: 13, name: "Project M", team: "Customer Success Team", details: "Client satisfaction initiative", status: "on hold", members: 7, type: "customer success" },
  { id: 14, name: "Project N", team: "Security Team", details: "Cybersecurity audit", status: "in progress", members: 6, type: "security" },
  { id: 15, name: "Project O", team: "Training Team", details: "Employee skills enhancement", status: "completed", members: 5, type: "training" },
];

const router = new Router();

router.get('/projects', ctx => {
  ctx.response.body = projects;
  ctx.response.status = 200;
});

router.get('/inProgress', ctx => {
  ctx.response.body = projects.filter(project => project.status == "in progress");
  ctx.response.status = 200;
});

router.get('/allProjects', ctx => {
  ctx.response.body = projects;
  ctx.response.status = 200;
});

router.get('/project/:id', ctx => {
  // console.log("ctx: " + JSON.stringify(ctx));
  const headers = ctx.params;
  // console.log("body: " + JSON.stringify(headers));
  const id = headers.id;
  if (typeof id !== 'undefined') {
    const index = projects.findIndex(project => project.id == id);
    if (index === -1) {
      const msg = "No entity with id: " + id;
      console.log(msg);
      ctx.response.body = { text: msg };
      ctx.response.status = 404;
    } else {
      let project = projects[index];
      ctx.response.body = project;
      ctx.response.status = 200;
    }
  } else {
    ctx.response.body = { text: 'Id missing or invalid' };
    ctx.response.status = 404;
  }
});

const broadcast = (data) =>
  wss.clients.forEach((client) => {
    if (client.readyState === WebSocket.OPEN) {
      client.send(JSON.stringify(data));
    }
  });

router.post('/project', ctx => {
  // console.log("ctx: " + JSON.stringify(ctx));
  const headers = ctx.request.body;
  // console.log("body: " + JSON.stringify(headers));
  const name = headers.name;
  const team = headers.team;
  const details = headers.details;
  const status = headers.status;
  const members = headers.members;
  const type = headers.type;
  if (typeof name !== 'undefined'
    && typeof team !== 'undefined'
    && typeof details !== 'undefined'
    && typeof status !== 'undefined'
    && typeof members !== 'undefined'
    && typeof type !== 'undefined') {
    const index = projects.findIndex(project => project.name == name && project.team == team);
    if (index !== -1) {
      const msg = "The entity already exists!";
      console.log(msg);
      ctx.response.body = { text: msg };
      ctx.response.status = 404;
    } else {
      let maxId = Math.max.apply(Math, projects.map(project => project.id)) + 1;
      let project = {
        id: maxId,
        name,
        team,
        details,
        status,
        members,
        type
      };
      projects.push(project);
      broadcast(project);
      ctx.response.body = project;
      ctx.response.status = 200;
    }
  } else {
    const msg = "Missing or invalid name: " + name + " team: " + team + " details: " + details
      + " status: " + status + " members: " + members + " type: " + type;
    console.log(msg);
    ctx.response.body = { text: msg };
    ctx.response.status = 404;
  }
});

router.put('/enroll/:id', ctx => {
  const headers = ctx.params;
  const id = headers.id;
  if (typeof id !== 'undefined') {
    const index = projects.findIndex(project => project.id == id);
    if (index === -1) {
      const msg = "No entity with id: " + id;
      console.log(msg);
      ctx.response.body = { text: msg };
      ctx.response.status = 404;
    } else {
      let project = projects[index];
      project.members++;
      ctx.response.body = project;
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