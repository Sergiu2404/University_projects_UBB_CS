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

const symptoms = [
  {id: 1, date: "2022-12-31", symptom: "Headache", medication: "Ibuprofen", dosage: "200mg", doctor: "Dr. Smith", notes: "Taken twice a day" },
  {id: 2, date: "2022-12-31", symptom: "Stomach Ache", medication: "Pepto Bismol", dosage: "2 tablets", doctor: "Dr. Smith", notes: "Taken after meals" },
  {id: 3, date: "2023-01-01", symptom: "Fever", medication: "Acetaminophen", dosage: "500mg", doctor: "Dr. Johnson", notes: "Taken every 4 hours" },
  {id: 4, date: "2023-01-01", symptom: "Sore Throat", medication: "Throat Lozenges", dosage: "As needed", doctor: "Dr. Johnson", notes: "Every 2 hours" },
  {id: 5, date: "2023-01-02", symptom: "Cough", medication: "Cough Syrup", dosage: "2 teaspoons", doctor: "Dr. Davis", notes: "Taken twice a day" },
  {id: 6, date: "2023-01-02", symptom: "Runny Nose", medication: "Nasal Spray", dosage: "As needed", doctor: "Dr. Davis", notes: "Spray in each nostril every 4 hours" },
  {id: 7, date: "2023-01-03", symptom: "Muscle Pain", medication: "Ibuprofen", dosage: "200mg", doctor: "Dr. Brown", notes: "Taken twice a day" },
  {id: 8, date: "2023-01-03", symptom: "Back Pain", medication: "Ibuprofen", dosage: "200mg", doctor: "Dr. Brown", notes: "Taken twice a day" },
  {id: 9, date: "2023-01-04", symptom: "Diarrhea", medication: "Imodium", dosage: "2 tablets", doctor: "Dr. Wilson", notes: "Taken after each loose stool" },
  {id: 10, date: "2023-01-04", symptom: "Nausea", medication: "Pepto Bismol", dosage: "2 tablets", doctor: "Dr. Wilson", notes: "Taken after meals" },
  {id: 11, date: "2023-01-05", symptom: "Headache", medication: "Ibuprofen", dosage: "200mg", doctor: "Dr. Smith", notes: "Taken twice a day" },
  {id: 12, date: "2023-01-05", symptom: "Stomach Ache", medication: "Pepto Bismol", dosage: "2 tablets", doctor: "Dr. Smith", notes: "Taken after meals" },
  {id: 13, date: "2023-01-06", symptom: "Fever", medication: "Acetaminophen", dosage: "500mg", doctor: "Dr. Johnson", notes: "Taken every 4 hours" },
  {id: 14, date: "2023-01-06", symptom: "Sore Throat", medication: "Throat Lozenges", dosage: "As needed", doctor: "Dr. Johnson", notes: "Every 2 hours" },
  {id: 15, date: "2023-01-15", symptom: "Back pain", medication: "Naproxen", dosage: "500mg", doctor: "Dr. Smith", notes: "Taken in the morning" },
  {id: 16, date: "2023-01-16", symptom: "Fever", medication: "Acetaminophen", dosage: "325mg", doctor: "Dr. Johnson", notes: "Taken every 4 hours" },
  {id: 17, date: "2023-01-17", symptom: "Sore throat", medication: "Amoxicillin", dosage: "500mg", doctor: "Dr. Johnson", notes: "Taken twice a day with food" },
  {id: 18, date: "2023-01-18", symptom: "Stomach ache", medication: "Pepto-Bismol", dosage: "2 tablets", doctor: "Dr. Smith", notes: "Taken after each meal" },
  {id: 19, date: "2023-01-19", symptom: "Allergic reaction", medication: "Benadryl", dosage: "25mg", doctor: "Dr. Johnson", notes: "Taken as needed" },
  {id: 20, date: "2023-01-20", symptom: "Cough", medication: "Robitussin", dosage: "1 tablespoon", doctor: "Dr. Smith", notes: "Taken every 4 hours" },
  {id: 21, date: "2023-01-21", symptom: "Fatigue", medication: "Iron supplement", dosage: "325mg", doctor: "Dr. Johnson", notes: "Taken in the morning with food" },
  {id: 22, date: "2023-01-22", symptom: "Anxiety", medication: "Alprazolam", dosage: "0.25mg", doctor: "Dr. Smith", notes: "Taken as needed" },
  {id: 23, date: "2023-01-23", symptom: "Insomnia", medication: "Doxylamine", dosage: "25mg", doctor: "Dr. Johnson", notes: "Taken at bedtime" },
  {id: 24, date: "2023-01-24", symptom: "Nausea", medication: "Metoclopramide", dosage: "10mg", doctor: "Dr. Smith", notes: "Taken before each meal" },
  {id: 25, date: "2023-02-01", symptom: "Headache", medication: "Ibuprofen", dosage: "200mg", doctor: "Dr. Johnson", notes: "Taken after dinner" },
  {id: 26, date: "2023-02-06", symptom: "Sore Throat", medication: "Throat Lozenges", dosage: "As needed", doctor: "Dr. Johnson", notes: "Every 2 hours" },
  {id: 27, date: "2023-02-15", symptom: "Back pain", medication: "Naproxen", dosage: "500mg", doctor: "Dr. Smith", notes: "Taken in the morning" },
  {id: 28, date: "2023-02-16", symptom: "Fever", medication: "Acetaminophen", dosage: "325mg", doctor: "Dr. Johnson", notes: "Taken every 4 hours" },
];

const router = new Router();
router.get('/days', ctx => {
  const dates = symptoms.map(symptom => symptom.date);
  const uniqueDates = new Set(dates);
  ctx.response.body = [...uniqueDates];
  ctx.response.status = 200;
});

router.get('/symptoms/:date', ctx => {
  // console.log("ctx: " + JSON.stringify(ctx));
  const headers = ctx.params;
  const date = headers.date;
  // console.log("category: " + JSON.stringify(category));
  ctx.response.body = symptoms.filter(symptom => symptom.date == date);
  // console.log("body: " + JSON.stringify(ctx.response.body));
  ctx.response.status = 200;
});

router.get('/entries', ctx => {
  ctx.response.body = symptoms;
  ctx.response.status = 200;
});

const broadcast = (data) =>
  wss.clients.forEach((client) => {
    if (client.readyState === WebSocket.OPEN) {
      client.send(JSON.stringify(data));
    }
  });

router.post('/symptom', ctx => {
  // console.log("ctx: " + JSON.stringify(ctx));
  const headers = ctx.request.body;
  // console.log("body: " + JSON.stringify(headers));
  const date = headers.date;
  const symptomName = headers.symptom;
  const medication = headers.medication;
  const dosage = headers.dosage;
  const doctor = headers.doctor;
  const notes = headers.notes;
  if (typeof date !== 'undefined'
    && typeof symptomName !== 'undefined'
    && typeof medication !== 'undefined'
    && typeof dosage !== 'undefined'
    && typeof doctor !== 'undefined'
    && typeof notes !== 'undefined') {
    const index = symptoms.findIndex(symptom => symptom.date == date && symptom.symptom == symptomName);
    if (index !== -1) {
      const msg = "Symptom already exists!";
      console.log(msg);
      ctx.response.body = { text: msg };
      ctx.response.status = 404;
    } else {
      let maxId = Math.max.apply(Math, symptoms.map(symptom => symptom.id)) + 1;
      let symptom = {
        id: maxId,
        date,
        symptom: symptomName,
        medication,
        dosage,
        doctor,
        notes
      };
      symptoms.push(symptom);
      broadcast(symptom);
      ctx.response.body = symptom;
      ctx.response.status = 200;
    }
  } else {
    const msg = "Missing or invalid date: " + date + " symptom: " + symptom
      + " medication: " + medication + " dosage: " + dosage + " doctor: " + doctor + " notes: " + notes;
    console.log(msg);
    ctx.response.body = { text: msg };
    ctx.response.status = 404;
  }
});

router.del('/symptom/:id', ctx => {
  // console.log("ctx: " + JSON.stringify(ctx));
  const headers = ctx.params;
  // console.log("body: " + JSON.stringify(headers));
  const id = headers.id;
  if (typeof id !== 'undefined') {
    const index = symptoms.findIndex(symptom => symptom.id == id);
    if (index === -1) {
      const msg = "No symptom with id: " + id;
      console.log(msg);
      ctx.response.body = { text: msg };
      ctx.response.status = 404;
    } else {
      let symptom = symptoms[index];
      symptoms.splice(index, 1);
      ctx.response.body = symptom;
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