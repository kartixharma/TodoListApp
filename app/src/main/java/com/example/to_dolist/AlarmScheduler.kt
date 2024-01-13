package com.example.to_dolist

interface AlarmSch {
      fun schedule(item: AlarmItem)
      fun cancel(item: AlarmItem)
}