using System.Collections;
using System.Collections.Generic;
using UnityEngine;

/**
 * \author   Jhomar Besmens
 * \version 1.0
 * \date    2017-07-22
 *
 * \brief   This class add preselected items to the inventory and destroy the current gameobject from the scene.     
 */
public class PickableItem : MonoBehaviour {

    public InventoryItem inventoryItem; /*!< Variable for the preselected items */
    private Inventory inventory;    /*!< Variable for the Inventory */
    private Transform player;   /*< Variable for the x,y,z coordinates from the player */

    /**
     * This method gets the x,yz coordinates from the player object and the Inventory component
     */
    private void Start()
    {
        player = GameObject.FindGameObjectWithTag("Player").transform;
        inventory = player.GetComponent<Inventory>();
    }

    /**
     * This method calls the AddItem function from the Inventory class and pass the inventoryItem to it and then destroys the gamobject himself
     */
    public void OnClickToInventory()
    {
        if (inventory.AddItem(inventoryItem))
        {
            Destroy(gameObject);
        }
    }

}
