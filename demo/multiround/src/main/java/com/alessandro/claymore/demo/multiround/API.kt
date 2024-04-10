package com.alessandro.claymore.demo.multiround

import kotlin.reflect.KClass

/*
    Il processor mantiene una mappa <Class, Class> = <Activity, Annotation>
    Ad ogni round il processor cerca per tutte le interfacce annotate con AutoProvides.

    Per prima cosa controlla se nella mappa c'è l'activity (passata come argument dell'annotation)
    tra le chiavi.

    Se non c'è allora deferra questa annotation al turno successivo, e genera una nuova annotation.
    Salva quindi nella mappa l'entry: { key: activity, value: annotation appena generata}.
 */
