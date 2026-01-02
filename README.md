<h1 align="center">JeuNerveux</h1>

<h2>Français</h2>

<h3>Map</h3>

<ol>
<li>La largeur d'une map est repérée par son nombre de colonnes</li>
<li>La hauteur d'une map est repérée par son nombre de lignes</li>
<li>Le nombre de layer correspond au nombre d'images qu'on peut accumuler sur une tuile</li>
</ol>

<h3>Système de positionnement</h3>

<ul>
<li>On défini un point de repère propre à chaque entité qui sert à faire tous les calculs, il est appelé <b>centre absolu</b></li>
<li>On réalise l'affichage d'une entité en décalant l'image vers la gauche et le haut pour que le centre de l'image corresponde avec le centre absolu</li>
<li>On réalise les calculs de collision en effectuant les mêmes décalages pour faire correspondre avec l'image</li>
<li>La caméra est positionnée sur le centre absolu, et donc sur le centre de l'image</li>
<li>La position (précision = pixel) d'une entité est donc son centre absolu</li>
<li>La position (précision = tuile) d'une entité est aussi la position de son centre absolu</li>
</ul>

<h3>Système d'affichage</h3>

<ul>
<li>Un objet de type <b>ITrackable</b> est choisi pour servir d'objet à suivre par la caméra</li>
<li>Cet objet est toujours au centre de la caméra sauf aux bords de la map</li>
<li>Les autres objets (tuiles, entités, ...) sont affichés en calculant leur position par rapport à l'objet traqué</li>
<li>Seul les objets dans le champ de vision de la caméra sont réellement affichés</li>
</ul>

<h3>UI</h3>

<ul>
<li>Un UI est positionné grâce à son centre absolu et sa taille</li>
<li>Si on met à <b>drawCentered=true</b> alors son centre d'affichage et son centre absolu correspondent</li>
<li>Sinon le centre absolu correspond avec le coin supérieur gauche de l'affichage</li>
</ul>

<h3>Frame</h3>

<ul>
<li>Une frame contient d'autres <b>UIObjects</b></li>
<li>Elle permet d'organiser harmonieusement les UIObjects en calculant les positions des UIObjects en fonction 
de leur taille, de leur nombre et d'où on veut les positionner</li>
<li>Une frame <b>drawCentered=true</b> affiche ses UIObjects autour de son centre absolu</li>
<li>Une frame <b>drawCentered=false</b> affiche ses UIObjects en x et y supérieurs au centre absolu</li>
<li>Une frame affiche toujours ses UIObjects en <b>drawCentered=true</b></li>
</ul>

<h3>Simulation</h3>

<ul>
<li>Tous les objets nécessitant une update sont mis à jour toutes les frames même si ils ne sont pas affichés</li>
</ul>

<h3>Future</h3>

<ul>
<li>Système de réutilisation des entités tuées (réutiliser la variable pour réduire le travail du garbage collector)</li>
</ul>