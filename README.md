# website-observer

Periodically checks configured web pages for changes. Sends notification via Telegram if changes are detected.

## Background story

Before covid I really wanted to get tickets for a popular music festival. Tickets for this festival are very rare and sold out in minutes. So I had to be really fast.

In the year I wanted to get the tickets, the organizers created a completely black page before the sale started. So nobody knows the exact time the sale would have started.

So I created a little (non public) PHP script which periodically checks the black page for changes. If any change happened, I got a message via Telegram.
There were multiple changes and it was really exciting. Finally I got the tickets but had no advantage before others because after the "black screen phase" they announced the date of the sale which was a few weeks later.

After that I reused this script on multiple occasions. But it had some bugs.
So I decided to port this script to spring-boot, fix the bugs and add some features and here we are.

## Setup

1. Clone this repo
2. Run `mvn clean install`
3. Create and configure your application.yml (see `src/main/resources/example.application.yml` and the Configuration part of this README)

### Configuration

You need to create and configure a `src/main/resources/application.yml` before running this application. 

You can find comments as documentation in the example file `src/main/resources/example.application.yml`.

The example I used checks my GitHub profile for changes. You get notified if some changes are detected on my GitHub profile.

## Run

Run `mvn spring-boot:run` to run the application

## Disclaimer

Many websites don't allow to be observed by such an application / script since its creating a lot of traffic. So I don't recommend to use this application and also not liable for your use.
I don't even know if github (my example) allows you to use this.