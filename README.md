# CS 160 Final Project Report


## Group Members



* Pranav Kalyan: Developer/QA engineer
* Dylan Supencheck: Designer/Developer
* Lucy White: Lead designer
* Benson Yi: Lead developer


## Contributions

We believed that everyone contributed equally to this project since we split the tasks. Benson did the guessing feature and he and Dylan worked on integrating Firebase into our implementation more completely. Lucy worked on the presentation poster as well as implementing feedback from the poster session and the final report. Pranav contributed heavily to writing the final report.


## Prototype Video (Link)

https://drive.google.com/file/d/1DZs2GUNHaYqrlKhDNw4CH1P61zCgeuUY/view?usp=sharing


## Problem & Solution Overview

The problem we began with is one familiar to many. Many people have been avid readers at one point in their life, especially childhood. However, many of these same people have found themselves in a place of being both unmotivated and too busy to now read regularly, even if reading was something they once enjoyed.  Our goal then was to make an app that helps people incorporate reading into their busy daily lives. We looked into why that happened through user interviews and found out that the most difficult part of reading was starting and finding a book. Our goal with Bookle was to make this process as easy and fun as possible. We were inspired by Wordle which gained such popularity despite having such a simple premise. Our app delivers a daily excerpt to the user in order to rekindle their passion for reading. The user can customize their reading experience by changing font size and dark or light mode. Also, once they have finished reading their daily excerpt, they can guess the book it is from. We believe this will give the user an additional challenge as well as creating further intrigue for the book. The user can also view the past bookles they completed, and can easily find a way to buy them through our bookshelf feature. Our app solves our problem by making reading a small daily habit, encouraging discovery, and being fun and easy to use. 


## Tasks


### Task 1: Read a bookle (easy)

The most front-facing task for the user is reading their first bookle. To do this, they can click on the book cover on the homescreen to open the reader. The reader itself is quite easy to navigate – they can scroll down to view the text, and an options button on the top right will let them toggle dark mode or resize the text as they desire.


### Task 2: Find/read a past bookle (medium)

The second task is opening the bookshelf to view bookles from past days. From the bookshelf, they can click on the book cover they desire which brings them to a page with more information about the book, when that bookle was available, as well as an Amazon link and share button they can tap. If the user taps on the book’s cover they are taken to the reader with the chapter from the book. 


### Task 3: Guess/buy/share today’s bookle (hard)

The third task is buying/sharing the current bookle. From the reader, users can scroll all the way down to the “Reveal” button. Pressing it will allow the user to enter a guess for the book’s title, and after they submit, they are brought to a screen where the bookle of the day is revealed, along with a button that will open an Amazon link. They can also hit a share button that will clip the first sentence of the day’s bookle to their clipboard and a link to Bookle (currently a github link)  that they can share on social media. The mystery cover on the home page and the bookshelf are then replaced with the revealed book. 


## Revised Interface Design

Feedback we received from users at the poster session fell into two categories: feedback on the flow and usability of the app, and suggestions for additional features or extensions on current features. Regarding the usability of the app, users suggested changes that logically made a lot of sense and fell in line with their expectations. Based off of their feedback, we made the mystery book in the bookshelf navigate to the reader when tapped, we saved past bookle’s in Firebase and navigated to the correct text from the past bookle’s page, we made the “Bookle” heading in all parts of the app navigate to the home page when tapped, we removed the reveal button from the reader after the book is revealed, we added a link to Bookle (currently just the Bookle github repository) to the text that is copied to the user’s clipboard when they tap a share button, and we edited the image used for the mystery book to look like the corner of it’s cover is being turned, in hopes of making it obvious that the the image affords tapping. 

The feature that underwent the greatest change from our last prototype is the bookshelf. We first wanted to improve the appearance of the bookshelf by making the heading more clear. We added a background (wood to convey the bookshelf metaphor) and made the text white, larger, and bold. We changed the header text to be the range of dates in the week associated with the section, instead of just “week X”. We changed the background to white to match the previous pages. Then we fixed the separation of the sections so that the top section includes only the bookles from the week in progress, and the lower section are past bookles. 

New message	

<img src="https://github.com/yibenson/Bookle/blob/master/app/src/main/res/images/image4.png" alt="drawing" width="200"/>

New mystery book

<img src="https://github.com/yibenson/Bookle/blob/master/app/src/main/res/images/image6.png" alt="drawing" width="200"/>

New guessing feature

<img src="https://github.com/yibenson/Bookle/blob/master/app/src/main/res/images/image1.png" alt="drawing" width="200"/>
  
New bookshelf design

<img src="https://github.com/yibenson/Bookle/blob/master/app/src/main/res/images/image5.png" alt="drawing" width="200"/>


The primary feedback we received from users was the desire for more interactive and customizable features. Many users expressed wanting to guess the book before revealing it, and we believed that would incite a feeling of fun competitiveness between users and further encourage sharing, like Wordle does. So, we added guessing after the user reads the chapter but before they reveal the book’s identity. Currently, the user has 6 attempts to guess the book, but the string they enter has to match the book’s title exactly for it to count as correct. We plan on making this implementation more accommodating to typos in the future, perhaps with a dropdown menu similar to the game Heardle.


### Task Storyboards

**Task 1 Storyboard: Read a bookle (easy)**


<table>
  <tr>
   <td>

<p align="center">
<img src="https://github.com/yibenson/Bookle/blob/master/app/src/main/res/images/image8.png" alt="drawing" class="center" width="150"/>
</p align="center">

<p>
<em>1: Maria is a brand new Bookle user. She opens the app for the first time, and sees the home screen. Following the focal point of the app, she taps on the mystery book in the middle of the screen.</em>
   </td>
   <td>

<p align="center">
<img src="https://github.com/yibenson/Bookle/blob/master/app/src/main/res/images/image6.png" alt="drawing" class="center" width="150"/>
</p align="center">

<p>
<em>2: Maria made it to the reading page; but she doesn’t like darkmode, and she finds the text a little too small. She sees a familiar icon used in her favorite e-reader app to change text settings and taps it.</em>
   </td>
   <td>
<p align="center">
<img src="https://github.com/yibenson/Bookle/blob/master/app/src/main/res/images/image11.png" alt="drawing" class="center" width="150"/>
</p align="center">

<p>
<em>3: First, Maria wants to turn off dark mode. She glances upon the “Dark mode toggle” switch and taps it.</em>
   </td>
  </tr>
</table>



<table>
  <tr>
   <td>

<p align="center">
<img src="https://github.com/yibenson/Bookle/blob/master/app/src/main/res/images/image16.png" alt="drawing" class="center" width="150"/>
</p align="center">

<p>
<em>4: Dark mode is gone, and Maria can now see the light! Not liking the old text size, Maria also slides the text size slider to the right, towards the larger A.</em>
   </td>
   <td>

<p align="center">
<img src="https://github.com/yibenson/Bookle/blob/master/app/src/main/res/images/image13.png" alt="drawing" class="center" width="150"/>
</p align="center">

<p>

<em>5: Now, Maria is reading her first bookle, and it is displayed to her liking. Hooray!</em>
   </td>
  </tr>
</table>


**Task 2 Storyboard: find a past bookle (medium)**



<p align="center">
<img src="https://github.com/yibenson/Bookle/blob/master/app/src/main/res/images/image10.jpg" alt="drawing" class="center" width="500"/>
</p align="center">


**Task 3 Storyboard: buy/share today’s bookle (hard)**


<p align="center">
<img src="https://github.com/yibenson/Bookle/blob/master/app/src/main/res/images/image12.jpg" alt="drawing" class="center" width="500"/>
</p align="center">


## Prototype Overview


### UI Overview

When you first open the app, the starting page is just the mystery book icon. This design decision was meant to encourage more experimentation by the user and also give the user a sense of wonder about the book. The corner of the mystery book is flipped up to hint that the user should interact with the book. Once you read the bookle, you can guess the book’s title up to 6 times and hit reveal. This navigates to a page with more details of the bookle, including an image of the book cover as well a countdown to the next bookle. There is also a share button which allows for user interaction.

When the user clicks on the help icon in the top left of the home page, a prompt appears which gives the user information about the app and lists its features. The other icon in the top right of the home page, a bookshelf, navigates to the bookshelf. The bookshelf contains the covers of past bookles sorted by weeks, and is designed to look like a bookshelf.  When the user clicks on a cover of a book, they are redirected to a page which contains more information about the book, an amazon link to the book as well as a share button.

The most important feature of our app is accessed when the user hits the mystery book icon in the homepage. This leads them to the reader which contains the excerpt of the day. When scrolling down, the top bar is hidden so that as much text as possible is visible, but if the user scrolls up, the top bar is revealed. The top bar includes the back button in the upper left and the text options button in the upper right. The text option has a text resize feature which accommodates the vision and size preferences of each user, a dark mode toggle which applies to the whole app, and a serif/sans serif toggle for the reader’s text. The user escapes from the text option menu by tapping in the gray space outside of the menu. Once the user finishes reading the excerpt and hits reveal, they are given a pop up menu where they can enter their guesses of the book’s title up to times and then reveal the actual book. Then they are directed to the cover and title of the book along with an amazon link, share button, and countdown to the next bookle.


### Unimplemented Features

All major features stated in previous designs were able to be implemented. We received a lot of great suggestions at the poster session that we couldn’t implement before the final project was due, but we would like to in the future. These features include being able to favorite bookles or like/dislike them when they’re revealed, a swipe reading mode, serif and sans serif font options, and an audio version of the bookle. These additions all involved pretty substantial changes and/or additions to our prototype, and although they would be great additions, our prototype is functional and meets the purpose of Bookle without them.


Sketch of favoriting system in bookshelf

<p align="center">
<img src="https://github.com/yibenson/Bookle/blob/master/app/src/main/res/images/image7.jpg" alt="drawing" class="center" width="150"/>
</p align="center">
        
Sketch of liking/disliking in reveal page
<p align="center">
<img src="https://github.com/yibenson/Bookle/blob/master/app/src/main/res/images/image15.jpg" alt="drawing" class="center" width="150"/>
</p align="center">

Sketch of additional text options

<p align="center">
<img src="https://github.com/yibenson/Bookle/blob/master/app/src/main/res/images/image14.jpg" alt="drawing" class="center" width="150"/>
</p align="center">
           
Sketch of audio option in the home page

<p align="center">
<img src="https://github.com/yibenson/Bookle/blob/master/app/src/main/res/images/image2.jpg" alt="drawing" class="center" width="150"/>
</p align="center">


We did add guessing to our prototype, but currently we just compare the string entered by the user to the book’s title to determine if the user is correct, and the inputted string must match the title string exactly to be correct.  We want the guessing to be more like the guessing in Heardle, where we would store a database of books and give the user titles that match what they’re typing in in a pop-up menu that they have to select their final answer from, so that we won’t have to account for typos in the user’s entry. 


<p align="center">
<img src="https://github.com/yibenson/Bookle/blob/master/app/src/main/res/images/image9.jpg" alt="drawing" class="center" width="200"/>
</p align="center">


### Wizard of Oz-ing

Our last prototype left book cover images, authors/titles, and Amazon links as hardcoded values. However, our new prototype no longer has these Wizard of Oz-ed! Instead, cover images, titles/authors, and Amazon links are pulled from the Firebase for each day’s bookle, and for all bookles for the bookshelf. Additionally, the ereader pulls HTML-formatted text from Firebase for the given day. Now that all of this is implemented, our app is fully functional in its current form. The bookshelf and ereader both fully function, as do all other pages. 


### Third-Party Code Documentation

To create the SectionedRecyclerView for the bookshelf, we relied on this tutorial by Gabriel Mariotti: [https://gist.github.com/gabrielemariotti/e81e126227f8a4bb339c](https://gist.github.com/gabrielemariotti/e81e126227f8a4bb339c)

We otherwise relied on official documentation from Android Developers or Firebase to implement Firebase communication, dark/light mode toggle, text resizing, and all the other features in the app. 

To process Image URLs, which we used for book covers, we used Picasso, “A powerful image downloading and caching library for Android.”

[https://github.com/square/picasso](https://github.com/square/picasso) 

