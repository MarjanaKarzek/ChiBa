using System.Collections;
using System.Collections.Generic;
using UnityEngine;

/**
 * \author   Jhomar Besmens
 * \version 1.0
 * \date    2017-07-19
 *
 * \brief   This class creates an flash bang effect when the transition from the Intro_Scene to the Main_Scene happens.
 */
public class FadeMainScene : MonoBehaviour
{
    private CanvasGroup fadeGroup;  /*!< Variable for the CanvasGroup */
    private float fadeInSpeed = 0.15f;  /*!< Variable for the fade duration */

    /**
     * This method gets the CanvasGroup in the scene and starts then with a white screen.
     */
    private void Start()
    {
        fadeGroup = GetComponent<CanvasGroup>();

        fadeGroup.alpha = 1;
    }

    /**
     * This method takes the alpha value from the fadeGroup and substract the time since the transition multiplied by fadeInSpeeed.
     */
    private void Update()
    {
               fadeGroup.alpha = 1 - Time.timeSinceLevelLoad * fadeInSpeed;
    }

}
