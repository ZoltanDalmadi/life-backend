# life-backend
A [Spring](https://spring.io)-based `.lif` file parser service. These files are used in applications implementing [Conway's Game of Life](https://en.wikipedia.org/wiki/Conway's_Game_of_Life).

Clients can use these endpoints:
- `POST: /api/upload-file`
Upload a `.lif` file. You must provide the `cols` and `rows` query parameters as well, to indicate your _Game of Life_ universe dimensions. Example: `/api/upload-file?cols=80&rows=60`
- `GET: /api/get-state`
Requests the parsed `.lif` file. The response is in `JSON` format.

An example front-end client written in [Angular 2](https://angular.io/) can be found [here](https://github.com/ZoltanDalmadi/life-frontend).
