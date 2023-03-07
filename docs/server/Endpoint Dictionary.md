## Endpoints

### Boards
POST /board #creates board<br />
GET /board/{id} #gets board<br />
POST /board/{id}/list #creates new list<br />
DELETE /board/{id}/list/{id} #deletes list<br />

### Card Lists
POST /list/{id}/card #create card<br />
DELETE /list/{id}/card/{id} #delete card<br />
PATCH /list/{id} #edit card list<br />

### Card
PATCH /card/{id} #edit card<br />
POST /card/{id}/tag #create tag<br />
DELETE /card/{id}/tag #delete tag<br />
POST /card/{id}/tasks #create tasks<br />
DELETE /card/{id}/tasks/{id} #delete tasks<br />

### Tasks
PATCH /task/{id} #Edit task<br />

### Tags
PATCH /tag/{id} #Edit tag<br />