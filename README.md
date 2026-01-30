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
<li>Une frame est positionnée comme tous les autres UI, i.e. son coin supérieur gauche correspond au point à partir duquel on affiche une image...</li>
<li>Pour positionner une frame en <b>drawReference=DRAW_BOTTOM_RIGHT_CORNER</b> il faut placer le coin inférieur droit là où on veut trouver le centre absolu</li>
<li>La frame sera étendue alors dans le sens des X<0 et Y<0 car <b>drawReference=DRAW_BOTTOM_RIGHT_CORNER</b></li>
</ul>

<h3>Simulation</h3>

<ul>
<li>Tous les objets nécessitant une update sont mis à jour toutes les frames même si ils ne sont pas affichés</li>
</ul>

<h3>Future</h3>

<ul>
<li>Système de réutilisation des entités tuées (réutiliser la variable pour réduire le travail du garbage collector)</li>
</ul>

<h2>Comportements recherchés</h2>

<h3>Entités</h3>
<ul>
<li>Toute entité doit faire partie d'un entity group</li>
<li>Une entité ne peut pas appartenir à plusieurs entity groups</li>
<li>Il existe un entity group principal où toutes les entités sont par défaut</li>
<li>Une entité peut être déplacée d'un entity group à un autre</li>
<li>Les entités sont alors mises à jour à travers leur entity group respectifs</li>
<li>Les collisions sont gérées à travers les entity groups</li>
<li>Seuls les entités d'un même entity group génèrent des collisions entre elles</li>
<li>Il est possible de définir des collisions entre deux entity groups différents</li>
<li>Les entity group sont mis à jour à travers EntityManager</li>
<li>Les collisions entre entity groups sont gérées à travers EntityManager</li>
<li>Une entité ne doit être ajoutée qu'à un entity group pour exister, pas dans EntityManager</li>
</ul>

<h2>TODO</h2>
<ul>
<li>Adapter EntityManager etc pour être compatible avec le fonctionnement par entity group</li>
</ul>