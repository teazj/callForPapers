'use strict';

app.config(function ($translateProvider) {
  $translateProvider.translations('fr-FR', {
  "lang" : {
    "fr-FR" : "Français",
    "en-US" : "Anglais"
  },
  "step1": {
    "email": "Adresse email * :",
    "name": "Nom * :",
    "firstname": "Prénom * :",
    "phone": "Téléphone :",
    "company": "Entreprise :",
    "bio": "Bio * :",
    "social": "URL (Google, Github, etc) :"
  },
  "step2": {
    "name": "Nom de la session * :",
    "description": "Description * :",
    "references": "Références ou compléments d'informations :",
    "difficulty": "Difficulté * (Débutant, Confirmé, Expert) :",
    "track": "Track * :",
    "cospeaker": "Co-speaker :",
    "beginner": "Débutant",
    "confirmed": "Confirmé",
    "expert": "Expert",
    "tracks": {
      "cloud": "Cloud",
      "mobile": "Mobile",
      "web": "Web",
      "discovery": "Discovery",
      "codelab": "Codelab"
    }
  },
  "step3": {
    "financial": "Avez-vous besoin d’une aide financière ? * :",
    "labelTravel": "Voyage :",
    "travel": "J'ai besoin d’une aide financière pour le voyage.",
    "place": "D’où venez vous ? :",
    "labelHotel": "Hébergement",
    "hotel": "J'ai besoin d’une aide financière pour l’hotel.",
    "date": "Pour quelle(s) date(s) ? :"
  },
  "steps": {
    "previous": "Etape précédente",
    "next": "Etape suivante",
    "validate": "Valider",
    "yes": "Oui",
    "no": "Non",
    "step": "Etape",
    "done": "Terminé !",
    "required" : "* Champ requis."
  }
});
  $translateProvider.translations('en-US', {
  "lang" : {
    "fr-Fr" : "French",
    "en-US" : "English"
  },
  "step1": {
    "email": "Email *:",
    "name": "Name *:",
    "firstname": "Firstname *:",
    "phone": "Phone:",
    "company": "Company:",
    "bio": "Bio *:",
    "social": "URL (Google, Github, etc):"
  },
  "step2": {
    "name": "Session nam *",
    "description": "Description *:",
    "complement": "Recommendation and additional information:",
    "difficulty": "Difficulty * (Beginner, Confirmed, Expert):",
    "track": "Track *:",
    "cospeaker": "Co-speaker:",
    "beginner": "Beginner",
    "confirmed": "Confirmed",
    "expert": "Expert",
    "tracks": {
      "cloud": "Cloud",
      "mobile": "Mobile",
      "web": "Web",
      "discovery": "Discovery",
      "codelab": "Codelab"
    }
  },
  "step3": {
    "financial": "Do you need financial help ? *:",
    "labelTravel": "Travel",
    "travel": "I need financial help for the trip.",
    "date": "For whitch date(s) ?:",
    "labelHotel": "Housing",
    "hotel": "I need financial help for the hotel.",
    "place": "Where are you coming from ?:"
  },
  "steps": {
    "previous": "Previous step",
    "next": "Next step",
    "validate": "Submit",
    "yes": "Yes",
    "no": "No",
    "step": "Step",
    "done": "Done !",
    "required" : "* Required field."
  }
});
  $translateProvider.preferredLanguage('fr-FR');
});