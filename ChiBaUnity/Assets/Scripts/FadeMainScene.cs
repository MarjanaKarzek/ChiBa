using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class FadeMainScene : MonoBehaviour {

    private CanvasGroup fadeGroup;
    private float fadeInSpeed = 0.33f;

    private void Start()
    {
        // Grab the only CanvasGroup in the scene
        fadeGroup = GetComponent<CanvasGroup>();

        // Start with a white screen;
        fadeGroup.alpha = 1;
    }

    private void Update()
    {
        // Fade-in
        fadeGroup.alpha = 1 - Time.timeSinceLevelLoad * fadeInSpeed;
    }

}
