# DumbBot

A relatively dumb Slack bot to customize.

## How to make it work ?

Create a file called `local.conf` in `src/main/resources` containing
```
api {
  key = "API-TOKEN"
}
```

The `API-TOKEN` can be found at *https://myteam.slack.com/apps/manage/custom-integrations*. 
Just create a bot or use an existing one and take the value in *Integration Settings*.
![Integration Settings](/images/integration_settings.png)

Then the bot should be ready for work. Just type `sbt run` in your console at the root of the project and your bot will be alive.