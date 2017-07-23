using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;


/**
 * \author   Jhomar Besmens
 * \version 1.0
 * \date    2017-07-22
 *
 * \brief   This class add preselected items to the inventory.     
 */
public class DropItem : MonoBehaviour {

    public InventoryItem computer;  /*!< Variable for the computer item */
    public InventoryItem sunglasses;    /*!< Variable for the sunglass item */
    public InventoryItem umbrella;  /*!< Variable for the umbrella item */
    public InventoryItem ball;  /*!< Variable for the ball item */

    private Inventory inventory; /*!< Variable for Inventory */

    /**
     * This method finds the Inventory component of the gameobject player and checks if it contains a certain key of the curren items.
     * If it finds one it calls the spawnItem() method on it.
     */
    public void searchItemInInventory()
    {
        inventory = GameObject.Find("Player").GetComponent<Inventory>();

        Dictionary<InventoryItem, int> currentItems = inventory.items;

        if (currentItems.ContainsKey(computer))
        {
            spawnItem(computer);
        }

        else if (currentItems.ContainsKey(sunglasses))
        {
            spawnItem(sunglasses);
        }

        else if (currentItems.ContainsKey(umbrella))
        {
            spawnItem(umbrella);
        }

        else if (currentItems.ContainsKey(ball))
        {
            spawnItem(ball);
        }
    }

    /**
     * This method drops a InventoryItem in a defined area and removes it from the inventory.
     */
        private void spawnItem(InventoryItem preselectInventoryItem)
        {
            GameObject obj = Instantiate(preselectInventoryItem.prefab, transform.position, transform.rotation);
            obj.transform.position = new Vector3(Random.Range(-200.0f, 200.0f), 700, Random.Range(-200.0f, 200.0f));

            inventory.RemoveItem(preselectInventoryItem);
        }
}
