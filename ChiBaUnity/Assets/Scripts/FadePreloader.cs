using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class FadePreloader : MonoBehaviour {

  
    private float loadTime;
    private float minimumLogoTime = 6.0f;
    private CanvasGroup fadeGroup;

    private void Start()
    {
    
    fadeGroup = FindObjectOfType<CanvasGroup>();

        // Start with a white screen;
        fadeGroup.alpha = 1;

        // Pre load the next scene
        // ---

        // Show Logo for minimum time
        if (Time.time < minimumLogoTime)
            loadTime = minimumLogoTime;
        else
            loadTime = Time.time;
    }

    private void Update()
    {
        // Fade-in
        if(Time.time < minimumLogoTime)
        {
            fadeGroup.alpha = 1 - Time.time;
        }

        // Fade-out
        if(Time.time > minimumLogoTime && loadTime != 0)
        {
            fadeGroup.alpha = Time.time - minimumLogoTime;
            if(fadeGroup.alpha >=1)
            {
                SceneManager.LoadScene("Main_Scene");
            }
        }
    }
}
