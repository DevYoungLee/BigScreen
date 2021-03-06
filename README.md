# BigScreen
BigScreen is an Android Application written in Java that displays images and information about recent movies. <br />
<br >
The application follows an MVVM architecture <br />
The data is fetched in JSON format from TheMovieDb Api service using Retrofit. <br />
Picasso is used for image loading. <br />
Room and shared preferences are used for persistent storage <br />
Recyclerview with GridLayoutManager is used to display the list of titles <br />
<br />
The movie titles can be sorted by popular: <br />
![alt text](screenshots/main_screen_popular.png "The movie titles can be sorted by popular") <br />
<br />
The movie titles can also be sorted by top-rated: <br />
![alt text](screenshots/main_screen_top_rated.png "The movie titles can be sorted by top-rated") <br />
<br />
Movies can be favorited and persistently stored to be viewed at a later time or offline <br />
![alt text](screenshots/main_screen_favorites.png "The movie titles can be sorted by popular") <br />
<br />
Clicking on an image shows detailed information about the movie such as title, summary, rating, date released, and reviews. There is also a link to view the trailer on Youtube. <br />
![alt text](screenshots/detail_screen_top.png "top of detail screen") <br />
![alt text](screenshots/detail_screen_bottom.png "bottom of detail screen") <br />
<br />
To try out the application, sign up for an api key at https://www.themoviedb.org/documentation/api and enter your api key at network/NetworkApi <br />
Thank you for reading! <br />
Any suggestions will be greatly appreciated!
