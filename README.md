# Awake

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

Current problems:
1. Alarm is currently not exact (the alarm will sound within ~one minute of set time)
2. Should allow canceling when editing or updating alarm.
3. If user exits challenge activity, openning the app should automatically bring up the challenge screen instead of the MainActivity.

Future plans:
1. Fix all our current problems. 
2. Beyond setting the alarm, we need to implement customization of the alarm such as repeat, sound, name, and challenge.
3. We need to implement the shake feature for challenges.
4. We need to implement the flashcard feature for challenges.
5. We need to imlement setting up a database for challenges. 
