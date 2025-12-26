<h1 align="center">JeuNerveux</h1>

<h2>Français</h2>

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

<h3>Simulation</h3>

<ul>
<li>Tous les objets nécéssitant une update sont mis à jour toutes les frames même si ils ne sont pas affichés</li>
</ul>

<h3>Future</h3>

<ul>
<li>Système de réutilisation des entités tuées (réutiliser la variable pour réduire le travail du garbage collector)</li>
</ul>