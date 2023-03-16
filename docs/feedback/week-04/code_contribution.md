# Code Contributions and Code Reviews

#### Focused Commits

Feedback: Excellent, the commits are small and concise oneliners that summarize the changes made to the system. Good job.

#### Isolation

Feedback: Excellent, each feature has its own branch.


#### Reviewability

Feedback: Excellent, MR's have a clear focus that becomes clear from the title and the description.

Mr's cover a small amount of commits and the deviation to main is small.

Nothing much to say hear good job.


#### Code Reviews

Feedback: Good, Mrs don't stay open for too long and there are great constructive and goal oriented commments.
But try to have a back and forth discussion going on, in MR [!32](https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-37/-/merge_requests/32) feedback has been given but no conformation has been given.
Here you could say something small like, "thnx I changed cardid to cardID for more consistency".

Also try to leave a comment for each MR, it's fine to leave a small comment like "Looks good, controller has been moved", for MRs like [!39](https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-37/-/merge_requests/39/diffs) but leave a small comment. This has to be done especially when the pipeline fail [!26](https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-37/-/merge_requests/26);


#### Build Server

Feedback: Sufficient, the pipeline for the main branch passes and the build duration are reasonable.

The pipeline fails quite often whether it is on the main branch or not.
Really check your build for checkstyle/tests before committing/merging. In MR [!25](https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-37/-/merge_requests/25) the pipeline failing is even mentioned but still approved and merged.
This should really not be happening, make sure that when a pipeline fails in main or in your other branches you fix it immediately.

In the last couple of builds there seems to be already some improvement though.
