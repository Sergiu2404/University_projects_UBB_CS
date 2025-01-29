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

const movies = [
  {
    id: 1,
    name: "The Shawshank Redemption",
    description: "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
    genre: "Drama",
    director: "Frank Darabont",
    year: 1994
  },
  {
    id: 2,
    name: "The Godfather",
    description: "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.",
    genre: "Crime",
    director: "Francis Ford Coppola",
    year: 1972
  },
  {
    id: 3,
    name: "The Dark Knight",
    description: "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, the caped crusader must come to terms with one of the greatest psychological tests of his ability to fight injustice.",
    genre: "Action",
    director: "Christopher Nolan",
    year: 2008
  },
  {
    id: 4,
    name: "The Godfather: Part II",
    description: "The early life and career of Vito Corleone in 1920s New York is portrayed while his son, Michael, expands and tightens his grip on his crime syndicate stretching from Lake Tahoe, Nevada to pre-revolution 1958 Cuba.",
    genre: "Crime",
    director: "Francis Ford Coppola",
    year: 1974
  },
  {
    id: 5,
    name: "The Lord of the Rings: The Return of the King",
    description: "Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring.",
    genre: "Action",
    director: "Peter Jackson",
    year: 2003
  },
  {
    id: 6,
    name: "Pulp Fiction",
    description: "The lives of two mob hitmen, a boxer, a gangster's wife, and a pair of diner bandits intertwine in four tales of violence and redemption.",
    genre: "Crime",
    director: "Quentin Tarantino",
    year: 1994
  },
  {
    id: 7,
    name: "The Lord of the Rings: The Fellowship of the Ring",
    description: "A meek hobbit of the Shire and eight companions set out on a journey to Mount Doom to destroy the One Ring and the dark lord Sauron.",
    genre: "Action",
    director: "Peter Jackson",
    year: 2001
  },
  {
    id: 8,
    name: "Forrest Gump",
    description: "Forrest Gump, while not intelligent, has accidentally been present at many historic moments, but his true love, Jenny Curran, eludes him.",
    genre: "Comedy",
    director: "Robert Zemeckis",
    year: 1994
  },
  {
    id: 9,
    name: "Inception",
    description: "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a CEO.",
    genre: "Action",
    director: "Christopher Nolan",
    year: 2010
  },
  {
    id: 10,
    name: "The Matrix",
    description: "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.",
    genre: "Action",
    director: "The Wachowskis",
    year: 1999
  },
  {
    id: 11,
    name: "Goodfellas",
    description: "Henry Hill and his friends work their way up through the mob hierarchy.",
    genre: "Crime",
    director: "Martin Scorsese",
    year: 1990
  },
  {
    id: 12,
    name: "Interstellar",
    description: "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.",
    genre: "Adventure",
    director: "Christopher Nolan",
    year: 2014
  },
  {
    id: 13,
    name: "The Silence of the Lambs",
    description: "A young FBI cadet must receive the help of an incarcerated and manipulative cannibal killer to help catch another serial killer, a madman who skins his victims.",
    genre: "Crime",
    director: "Jonathan Demme",
    year: 1991
  },
  {
    id: 14,
    name: "The Green Mile",
    description: "The lives of guards on Death Row are affected by one of their charges: a black man accused of child murder and rape, yet who has a mysterious gift.",
    genre: "Crime",
    director: "Frank Darabont",
    year: 1999
  }
];

const router = new Router();
router.get('/genres', ctx => {
  const genres = movies.map(movie => movie.genre);
  const uniqueGenres = new Set(genres);
  ctx.response.body = [...uniqueGenres];
  ctx.response.status = 200;
});

router.get('/movies/:genre', ctx => {
  // console.log("ctx: " + JSON.stringify(ctx));
  const headers = ctx.params;
  const genre = headers.genre;
  // console.log("category: " + JSON.stringify(category));
  ctx.response.body = movies.filter(movie => movie.genre == genre);
  // console.log("body: " + JSON.stringify(ctx.response.body));
  ctx.response.status = 200;
});

router.get('/all', ctx => {
  ctx.response.body = movies;
  ctx.response.status = 200;
});

const broadcast = (data) =>
  wss.clients.forEach((client) => {
    if (client.readyState === WebSocket.OPEN) {
      client.send(JSON.stringify(data));
    }
  });

router.post('/movie', ctx => {
  // console.log("ctx: " + JSON.stringify(ctx));
  const headers = ctx.request.body;
  // console.log("body: " + JSON.stringify(headers));
  const name = headers.name;
  const description = headers.description;
  const genre = headers.genre;
  const director = headers.director;
  const year = headers.year;
  if (typeof name !== 'undefined'
    && typeof description !== 'undefined'
    && typeof genre !== 'undefined'
    && typeof director !== 'undefined'
    && typeof year !== 'undefined') {
    const index = movies.findIndex(movie => movie.name == name);
    if (index !== -1) {
      const msg = "Movie already exists!";
      console.log(msg);
      ctx.response.body = { text: msg };
      ctx.response.status = 404;
    } else {
      let maxId = Math.max.apply(Math, movies.map(movie => movie.id)) + 1;
      let movie = {
        id: maxId,
        name,
        description,
        director,
        year,
        genre
      };
      movies.push(movie);
      broadcast(movie);
      ctx.response.body = movie;
      ctx.response.status = 200;
    }
  } else {
    const msg = "Missing or invalid name: " + name + " description: " + description 
    + " genre: " + genre + " director: " + director + " year: " + year;
    console.log(msg);
    ctx.response.body = { text: msg };
    ctx.response.status = 404;
  }
});

router.del('/movie/:id', ctx => {
  // console.log("ctx: " + JSON.stringify(ctx));
  const headers = ctx.params;
  // console.log("body: " + JSON.stringify(headers));
  const id = headers.id;
  if (typeof id !== 'undefined') {
    const index = movies.findIndex(movie => movie.id == id);
    if (index === -1) {
      const msg = "No movie with id: " + id;
      console.log(msg);
      ctx.response.body = { text: msg };
      ctx.response.status = 404;
    } else {
      let movie = movies[index];
      movies.splice(index, 1);
      ctx.response.body = movie;
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