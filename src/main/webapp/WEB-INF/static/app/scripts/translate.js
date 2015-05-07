'use strict';

app.config(function($translateProvider) {
  $translateProvider.translations('fr-FR', {
    "lang": {
      "fr-FR": "Français",
      "en-US": "Anglais"
    },
    "step1": {
      "email": "Adresse email * :",
      "name": "Nom * :",
      "firstname": "Prénom * :",
      "phone": "Téléphone :",
      "company": "Entreprise :",
      "bio": "Bio * :",
      "hintBio": "Décrivez vous en quelques mots. Cette description sera utilisée sur le site web",
      "social": "URL (Google, Github, etc) :",
      "hintSocial": "Donnez les liens de vos réseaux sociaux (pour le site web) : Twitter / G+ / Github / Blog"
    },
    "step2": {
      "name": "Nom de la conférence * :",
      "description": "Description * :",
      "hintDescription": "Donnez une description de votre présentation. Elle sera utilisée sur le site web",
      "references": "Références ou compléments d'informations :",
      "hintReferences": "Y a-t-il des conférences où vous avez déjà fait des présentations ? Si vous pouvez donner un lien vers celle(s)-ci ça serait bien.",
      "difficulty": "Difficulté * (Débutant, Confirmé, Expert) :",
      "track": "Track * :",
      "cospeaker": "Co-conférenciers :",
      "hintCospeaker": "Si vous n'êtes pas seul lors de la présentation, donnez les nom / email / bio / liens sociaux des autres conférenciers",
      "beginner": "Débutant",
      "confirmed": "Confirmé",
      "expert": "Expert",
      "tracks": {
        "cloud": "Cloud",
        "mobile": "Mobile",
        "web": "Web",
        "discovery": "Discovery",
        "codelab": "Codelab"
      },
      "hintTrack": "Choisissez la catégorie dans laquelle vous pensez que votre conférence se situe. Discovery est une catégorie pour les conférences non techniques."
    },
    "step3": {
      "header1": "Ici vous renseignerez les informations nécessaires pour votre venue. Ces informations serons très minucieusement étudiées pour notre décision. Ne choisissez oui que si vous en avez besoin.",
      "header2": "Un petit déjeuner et un déjeuner est offert le jour de l'évènement.",
      "financial": "Avez-vous besoin d’une aide financière ? * :",
      "labelTravel": "Voyage :",
      "travel": "J'ai besoin d’une aide financière pour le voyage.",
      "place": "D’où venez vous ? :",
      "labelHotel": "Hébergement",
      "hotel": "J'ai besoin d’une aide financière pour l’hotel.",
      "date": "Pour quelle(s) date(s) ? :",
      "sendError": "Error lors de l'envoi veuillez réessayer"
    },
    "steps": {
      "previous": "Etape précédente",
      "next": "Etape suivante",
      "validate": "Valider",
      "close": "Fermer",
      "yes": "Oui",
      "no": "Non",
      "step": "Etape",
      "done": "Terminé !",
      "required": "* Champ requis."
    },
    "result": {
      "success": "Bravo !",
      "successMessage": "Votre présentation a été envoyée. Vous recevrez bientôt un email de confirmation. Nous vous recontacterons dès que nous raurons fait notre choix."
    },
    "admin": {
      "logout" : "Se déconnecter",
      "session" : "Session",
      "sessions" : "Sessions",
      "administration" : "Administration",
      "toggle" : "Ouvrir le volet",
      "clearSorting" : "Annuler le tri",
      "clearFilter" : "Annuler les filtres",
      "speaker" : "Conférencier",
      "title" : "Titre",
      "difficulty" : "Difficulté",
      "track" : "Track",
      "description" : "Description",
      "mean" : "Moyenne",
      "date" : "Date",
      "deliberation" : "Délibération",
      "commentaries" : "Commentaires",
      "message" : "Message",
      "votes" : "Votes",
      "you" : "Vous",
      "financialHelp" : "Aide financière"
    },
    "error" : {
      "backendcommunication" : "Désolé, il y a eu un problème avec le serveur distant",
      "noInternet" : "Désolé, il y a eu une problème de connexion, êtes vous connecté à internet ?"
    },
    "login" : {
      "wait" : "Veuillez patienter"
    },
    "just_now" : "à l'instant",
    "seconds_ago" : "il y a {{time}} secondes",
    "a_minute_ago" : "il y a une minute",
    "minutes_ago" : "il y a {{time}} minutes",
    "an_hour_ago" : "il y a une heure",
    "hours_ago" : "il y a {{time}} heures",
    "a_day_ago" : "hier",
    "days_ago" : "il y a {{time}} jours",
    "a_week_ago" : "il y a une semaine",
    "weeks_ago" : "il y a {{time}} semaines",
    "a_month_ago" : "il y a un mois",
    "months_ago" : "il y a {{time}} mois",
    "a_year_ago" : "il y a un an",
    "years_ago" : "il y a {{time}} ans",
    "over_a_year_ago" : "il y a plus d'un an",
    "seconds_from_now" : "dans une seconde",
    "a_minute_from_now" : "dans une minute",
    "minutes_from_now" : "dans {{time}} minutes",
    "an_hour_from_now" : "dans une heure",
    "hours_from_now" : "dans {{time}} heures",
    "a_day_from_now" : "demain",
    "days_from_now" : "dans {{time}} jours",
    "a_week_from_now" : "dans une semaine",
    "weeks_from_now" : "dans {{time}} semaine",
    "a_month_from_now" : "dansun mois",
    "months_from_now" : "dans {{time}} mois",
    "a_year_from_now" : "dans un an",
    "years_from_now" : "dans {{time}} ans",
    "over_a_year_from_now" : "dans plus d'un an"
  });
  $translateProvider.translations('en-US', {
    "lang": {
      "fr-FR": "French",
      "en-US": "English"
    },
    "step1": {
      "email": "Email *:",
      "name": "Name *:",
      "firstname": "Firstname *:",
      "phone": "Phone:",
      "company": "Company:",
      "bio": "Bio *:",
      "hintBio": "Describe yourself with a few words. This description will be use to fill the website",
      "social": "URL (Google, Github, etc):",
      "hintSocial": "Give us your socials networks data (for the website) : Twitter / G+ / Github / Blog"

    },
    "step2": {
      "name": "Session name *",
      "description": "Description *:",
      "hintDescription": "Give a description of your talk. This description will be used to fill the website",
      "references": "References or complement informations :",
      "hintReferences": "Is there any conferences where you have already spoken ? If you could give a link to the presentation, it's better.",
      "complement": "Recommendation and additional information:",
      "difficulty": "Difficulty * (Beginner, Confirmed, Expert):",
      "track": "Track *:",
      "cospeaker": "Co-speaker:",
      "hintCospeaker": "If you are not alone on stage, give the co-speaker name / email / bio / social networks",
      "beginner": "Beginner",
      "confirmed": "Confirmed",
      "expert": "Expert",
      "tracks": {
        "cloud": "Cloud",
        "mobile": "Mobile",
        "web": "Web",
        "discovery": "Discovery",
        "codelab": "Codelab"
      },
      "hintTrack": "Choose the track where you think your talk will be place. Discovery is a track for non technicals presentations"
    },
    "step3": {
      "header1": "Here is all the informations relatives to your venue. The following informations will be carefuly study for our decision. So please select Yes, only if needed.",
      "header2": "A breakfast and lunch is offered the day of the event.",
      "financial": "Do you need financial help ? *:",
      "labelTravel": "Travel",
      "travel": "I need financial help for the trip.",
      "date": "For whitch date(s) ?:",
      "labelHotel": "Housing",
      "hotel": "I need financial help for the hotel.",
      "place": "Where are you coming from ?:",
      "sendError": "An error occurred during the submission, please retry."
    },
    "steps": {
      "previous": "Previous step",
      "next": "Next step",
      "validate": "Submit",
      "close": "Close",
      "yes": "Yes",
      "no": "No",
      "step": "Step",
      "done": "Done !",
      "required": "* Required field."
    },
    "result": {
      "success": "Well done !",
      "successMessage": "Your talk has been send. You will soon receive a confirmation email. We will contact you as soon as we will make our decision."
    },
    "admin": {
      "logout" : "Logout",
      "session" : "Session",
      "sessions" : "Sessions",
      "administration" : "Administration",
      "toggle" : "Toggle navigation",
      "clearSorting" : "Clear sorting",
      "clearFilter" : "Clear filters",
      "speaker" : "Speaker",
      "title" : "Title",
      "difficulty" : "Difficulty",
      "track" : "Track",
      "description" : "Description",
      "mean" : "Mean",
      "date" : "Date",
      "deliberation" : "Deliberation",
      "commentaries" : "Commentaries",
      "message" : "Message",
      "votes" : "Votes",
      "you" : "You",
      "financialHelp" : "Financial Help"
    },
    "error" : {
      "backendcommunication" : "Sorry, a problem occure with the server",
      "noInternet" : "Sorry, it seems that your are not connected to internet"
    },
    "login" : {
      "wait" : "Please wait"
    },
    "just_now" : "just now",
    "seconds_ago" : "{{time}} seconds ago",
    "a_minute_ago" : "a minute ago",
    "minutes_ago" : "{{time}} minutes ago",
    "an_hour_ago" : "an hour ago",
    "hours_ago" : "{{time}} hours ago",
    "a_day_ago" : "yesterday",
    "days_ago" : "{{time}} days ago",
    "a_week_ago" : "a week ago",
    "weeks_ago" : "{{time}} weeks ago",
    "a_month_ago" : "a month ago",
    "months_ago" : "{{time}} months ago",
    "a_year_ago" : "a year ago",
    "years_ago" : "{{time}} years ago",
    "over_a_year_ago" : "over a year ago",
    "seconds_from_now" : "{{time}} seconds from now",
    "a_minute_from_now" : "a minute from now",
    "minutes_from_now" : "{{time}} minutes from now",
    "an_hour_from_now" : "an hour from now",
    "hours_from_now" : "{{time}} hours from now",
    "a_day_from_now" : "tomorrow",
    "days_from_now" : "{{time}} days from now",
    "a_week_from_now" : "a week from now",
    "weeks_from_now" : "{{time}} weeks from now",
    "a_month_from_now" : "a month from now",
    "months_from_now" : "{{time}} months from now",
    "a_year_from_now" : "a year from now",
    "years_from_now" : "{{time}} years from now",
    "over_a_year_from_now" : "over a year from now"
  });
  $translateProvider.preferredLanguage('fr-FR');
});
