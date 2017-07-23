
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

/**
 * \mainpage Short Description
 * This is a students project of the University of Applied Sciences in Berlin. It belogs to the course "Entwicklung Multimediasysteme".
 * 
 * This documentation is about the Unity application based on the Google VR Cardboard SDK.
 */


/**
 * \author   Jhomar Besmens
 * \version 1.0
 * \date    2017-07-19
 * 
 * \brief   This class check and sets the state for the animation controller of the ChiBa avatar.
 */
public class ChiBaAnimator : MonoBehaviour
{
    private Animator anim;  /*!< Variable for the Animator Component */
    public float speed = 2.5f;  /*!< Speed for the animation duration. */
    private bool IsMoving;  /*!< Check if IsMoving true or false. */

    /**
     * Get Animator component on start.
     */
    void Start()
    {
        anim = GetComponent<Animator>();
    }

    /**
     * If IsMoving true then set animation in animation controller to IsWalking
     */
    void FixedUpdate()
    {
        IsMoving = GameObject.Find("Player").GetComponent<VRLookAndWalk>().moveForward;

        if (IsMoving)
        {
            anim.SetBool("IsWalking", true);
        }
        else
        {
            anim.SetBool("IsWalking", false);
        }
    }


}
