
****IN GENERAL you should always PULL your code before making any modifications then PUSH your code


**get the code for the first time(you only have to do this once)
git clone

****ALWAYS MAKE SURE THAT YOUR DIRECTORY IS ON ftc_app YOU DO NOT WANT TO PUSH YOUR USER***************
Change directory
cd ftc_app


****PULLING SECTION	Get the code after you cloned
git pull


****COMMITING SECTION
git add -A

git commit -m "SIMPLE COMMENT"


***** BRANCHES / MERGING
First, create a branch and switch to it 
(This command does both at the same time)

git checkout -b hotfix_servo

Next, make some changes, run some tests, etc..

Then switch back to the master branch

git checkout master

Finally, merge your changes

git merge hotfix_servo

Then you are ready to push

***** PUSHING SECTION

git push origin master





