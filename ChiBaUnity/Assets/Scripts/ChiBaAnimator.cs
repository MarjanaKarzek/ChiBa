
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ChiBaAnimator : MonoBehaviour {

    private Animator anim;
    public float speed = 2.5f;
    bool IsMoving;

	void Start ()
    {
        anim = GetComponent<Animator>();
        //IsMoving = GameObject.Find("Player").GetComponent<VRLookAndWalk>().moveForward;
	}

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
