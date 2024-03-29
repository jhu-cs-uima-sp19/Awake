# Awake
Awake is an alarm clock app that makes users complete a challenge before turning off an alarm. The goal is to help people wake up on time. Currently, the two challenges we plan to implement are the following:
1. A shaking exericse where the user shakes the phone a certain number of times to turn off the alarm
2. A flashcard challenge where users have to review a set of flashcards to turn off the alarm.

## Sprint 1:
Currently our app has the following features:
1. By hitting the floating action button, the user can set an alarm (hrs/min and AM/PM)
2. Turn off/on an alarm by tapping the switch next to an alarm.
3. Alarms in the list are organized by earliest alarm time to latest. 
4. Tap alarm in list to edit it. 
5. Editting an alarm automatically turns on alarm.
6. Long tap alarm to delete alarm (confirmation dialog will pop up)
7. When the alarm goes off, the user is directed to another screen and must complete the activity to turn off the alarm and return
   to the main activity.  
8. The current placeholder challenge is a tapping challenge; the user must tap a button 30 times to turn off alarm. 

Current problems:
1. Alarm is currently not exact (the alarm will sound within ~one minute of set time)
2. Should allow canceling when editing or updating alarm.
3. If user exits challenge activity, opening the app should automatically bring up the challenge screen instead of the MainActivity.

## Future Plans :
Before we conclude this project, we plan to complete the following tasks:
1. Fix all our current problems. 
2. Beyond setting the alarm, we need to implement customization of the alarm such as repeats, alarm sound, name, and challenge.
3. We need to implement the shake feature for challenges.
4. We need to implement the flashcard feature for challenges.
5. We need to set up a database for the flashcard challenge. 
6. We need to make the colors and fonts match with our original plan.

## Sprint 2:

After sprint 2, our app now has the following features:
1. Users can now customize alarms to include the following:
* Name of alarm
* Alarm sounds
* Day(s) of the week to repeat the alarm
* Whether they want to include a challenge or not
* Which challenge to include if a challenge is desired
2. Cancel editing or creating new alarms
3. Once the exercise activity is selected, the user will be taken to a new screen to set the number of shakes.
4. The exercise activity launches once the alarm rings and the shaking feature works properly.
* Note that the app is sensitive to changes in acceleration so violent shakes can cause the count to repeatly decrease, almost as if it is decreasing by increments of more than one.
5. Alarms can ring even when phone is off.
6. Once the flashcard activity is selected, the user will be taken to a new screen to manage flashcard sets, where the user can manage create/delete sets.
7. Upon clicking a set, the user will be able to access the cards of the set and modify their contents.
8. The flashcard activity launches once the alarm rings and displays cards, where the user must input the appropriate term. 
9. Once the set is complete, the user can shut off the alarm.

The tests we have conducted to verify the repeat function of the alarm:
1. Non-repeating alarms:
* Setting an alarm time in the past. Desired function is to then set the alarm for the next day.
* Setting an alarm time in the future. Desired function is to ring the alarm once then turn off the alarm.
2. Repeating alarms:
* Chaining repeats. Desired function is to properly set the next alarm after the first one rings.
* Repeating an alarm today but set the alarm time for the past. Next alarm should be one week from today.
* Repeating alarm today and one later but alarm time is set in the past. Next alarm should not be today.
* Repeating alarm today and one later but alarm time is set in the future. Next alarm should be today.


