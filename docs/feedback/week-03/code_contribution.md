# Code Contributions and Code Reviews

#### Focused Commits

Feedback: Good, Most commit messages are concise one liners, which clearly summarize the change.

The commits aren't too big, and they are coherent to the current system.

#### Isolation

Feedback: Good, So far feature branches have been used to isolate individual features, that have been implemented in main, during development.

#### Reviewability

Feedback: Good, MRs have a clear focus that becomes clear from the title and the description.

Make sure to check your pipelines before commiting. This way you end up with less format changes and less commits overall.

#### Code Reviews

Feedback: Good, In MR "
Basic class structure for commons in order to use our basic classes for the server and client.", the review has been done really well.

Make sure to do this for every MR, have a short discussion and write constructive feedback that is goal orientated.

If the MR is relatively small it's fine to write a shorter comment.

#### Build Server

Feedback: Insufficient/Sufficient, Make sure your pipelines really pass. Right now test_job seems to fail everytime.

This seems to be a coverage problem, which doesn't make the whole pipeline fail. 
But since you have this in your checkstyle you should try to fix this or change the checkstyle, it's only required to have 10+ checkstyle rules.